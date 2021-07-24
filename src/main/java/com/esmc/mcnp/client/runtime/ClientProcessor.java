/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.runtime;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author HP
 */
public class ClientProcessor {

    public static String lastRequest = "";
    public static String lastAnswer = "";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    static public String send(String ip, String port, CommunicationProcessType ctype, Object obj) throws Exception {
        try {
            lastRequest = SerializationTools.jsonSerialise(obj);
            lastRequest = lastRequest.replace("parameters", "params");
        } catch (Exception e) {
            throw new Exception("Serialisation Error(Server Side) : " + e.getMessage() + "\r\n obj:>" + ((Request) obj).getMethod());
        }
        System.out.println("Requete : " + lastRequest);
        lastAnswer = sendRequest(ctype, ip, port, lastRequest);
        System.out.println("Reponse : " + lastAnswer);
        Response ret = new Response();

        try {
            ret = (Response) SerializationTools.jsonDeserialise(lastAnswer, Response.class);
        } catch (Exception e) {
            throw new Exception("Serialisation Error : " + e.getMessage() + "\r\n answer:>" + lastAnswer + "<\r\n");
        }

        if (ret.getResult() == null) {
            System.out.println("Message : " + ret.getError().getMessage());
            return ret.getError().getMessage();
        } else {
            System.out.println("Message : " + ret.getResult());
            return ret.getResult();
        }
    }

    //
    // -------------------------------------------------------------------------------------
    //
    public static String sendRequest(CommunicationProcessType ctype, String ip, String port, String request) throws Exception {
        request = request.replace("parameters", "params");
        switch (ctype) {
            case TCP:
                return sendRequestTCP(ip, port, request);
            case PIPE:
                return sendRequestPipe(ip, port, request);
            default:
                throw new Exception("Unknown Serialization Process");
        }
    }

    public static String sendRequestTcp(String ip, String port, String request) {
        Integer iport = Integer.parseInt(port);
        String result = "";
        try (Socket clientSocket = new Socket(ip, iport)) {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ByteArrayOutputStream buffer;
            try (InputStream in = clientSocket.getInputStream()) {
                byte[] data = request.getBytes(UTF8_CHARSET);
                out.write(data, 0, data.length);
                buffer = new ByteArrayOutputStream();
                int nRead;
                boolean answer = false;
                byte[] rdata = new byte[1024];
                while (((nRead = in.read(rdata, 0, data.length)) != -1) || (!answer)) {
                    buffer.write(data, 0, nRead);
                    answer = true;
                }
            }
            buffer.flush();
            byte[] byteArray = buffer.toByteArray();
            result = new String(byteArray, UTF8_CHARSET);
        } catch (IOException ex) {
            Logger.getLogger(ClientProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static String sendRequestTCP(String ip, String port, String request) {
        String result = null;
        try {
            InetAddress serverIPAddress = InetAddress.getByName(ip);
            int iport = Integer.parseInt(port);
            InetSocketAddress serverAddress = new InetSocketAddress(
                    serverIPAddress, iport);
            Selector selector = Selector.open();
            try (SocketChannel channel = SocketChannel.open()) {
                channel.configureBlocking(false);
                channel.connect(serverAddress);
                int operations = SelectionKey.OP_CONNECT | SelectionKey.OP_READ
                        | SelectionKey.OP_WRITE;
                channel.register(selector, operations);
                boolean done = false;
                while (selector.select() > 0) {
                    done = processWriteSet(selector.selectedKeys(), request);
                    if (done) {
                        System.out.println("Breaking Write ...");
                        break;
                    }
                }
                if(done){
                    while (selector.select() > 0) {
                    result = processReadSet(selector.selectedKeys(), request);
                    if (StringUtils.isNotBlank(result)) {
                        System.out.println("Breaking Read...");
                        break;
                    }
                }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Result : " + result);
        return result;
    }

    public static String processReadSet(Set<SelectionKey> readySet, String request) throws Exception {
        Iterator<SelectionKey> iterator = readySet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            if (key.isConnectable()) {
                boolean connected = processConnect(key);
                if (!connected) {
                    return null; // Exit
                }
            }
            if (key.isReadable()) {
                String result = processRead(key);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        }
        return null;
    }

    public static boolean processWriteSet(Set<SelectionKey> readySet, String request) throws Exception {
        Iterator<SelectionKey> iterator = readySet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            if (key.isConnectable()) {
                boolean connected = processConnect(key);
                if (!connected) {
                    return true; // Exit
                }
            }
            if (key.isWritable()) {
                if (processWrite(key, request)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean processConnect(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            while (sc.isConnectionPending()) {
                sc.finishConnect();
            }
        } catch (IOException e) {
            key.cancel();
            return false;
        }
        return true;
    }

    public static boolean processWrite(SelectionKey key, String msg) throws IOException {
        System.out.println("Ecriture");
        SocketChannel sChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(UTF8_CHARSET));
        int wr = sChannel.write(buffer);
        if (wr > 0) {
            System.out.println("Ecriture Reussie : ");
            return true;
        }
        return false;
    }

    public static String processRead(SelectionKey key) throws Exception {
        System.out.println("Lecture");
        SocketChannel sChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int rd = sChannel.read(buffer);
        if (rd > 0) {
            System.out.println("Lecture reussie ");
            buffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            String msg = charBuffer.toString();
            return msg;
        } else {
            return "";
        }
    }

    public static String sendRequestPipe(String ip, String port, String request) {
        return "";
    }
}
