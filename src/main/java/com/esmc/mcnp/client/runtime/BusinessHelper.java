/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.runtime;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author HP
 */
public class BusinessHelper {

    public static String IP = "127.0.0.1";
    public static String Port = "18000";
    public static String CommType = "TCP";
    public static String Printer;
    public static final String MODEL = "Evolis Primacy";

    public static CommunicationProcessType getCommType() {
        return (CommType.equals("TCP"))
                ? CommunicationProcessType.TCP
                : CommunicationProcessType.PIPE;
    }

    public static String printerSetEvent(String sEvent, String printer) throws Exception {
        if (StringUtils.isBlank(sEvent) || StringUtils.isBlank(printer)) {
            throw new NullPointerException("BusinessHelper.PrinterSetEvent");
        }
        String id = "225";
        ParamsSupervision p = new ParamsSupervision(sEvent, printer);
        String result = ClientProcessor.send(IP, Port, getCommType(), new Supervision("SUPERVISION.SetEvent", id, p));
        Response answer = new Response();
        answer.setResult(result);
        return result;
    }

    public static String printerGetEvent(String printer) throws Exception {
        if (StringUtils.isBlank(printer)) {
            throw new NullPointerException("BusinessHelper.PrinterGetEvent");
        }
        String id = "222";
        ParamsSupervision p = new ParamsSupervision(printer);
        String result = ClientProcessor.send(
                IP, Port, getCommType(),
                new Supervision("SUPERVISION.GetEvent", id, p));
        return result;
    }

    public static String printerGetState(String printer) throws Exception {
        if (StringUtils.isBlank(printer)) {
            throw new NullPointerException("BusinessHelper.PrinterGetState");
        }
        String id = "223";
        ParamsSupervision p = new ParamsSupervision(printer);
        String result = ClientProcessor.send(
                IP, Port, getCommType(),
                new Supervision("SUPERVISION.GetState", id, p));

        return result;
    }

    public static String[] setSupervisedPrinter(String model, String level) {
        String[] res;
        try {
            if (StringUtils.isBlank(model)) {
                throw new NullPointerException("BusinessHelper.GetSupervisedPrinter");
            }
            String id = "220";
            ParamsSupervision p = new ParamsSupervision();
            p.setDevice(model);
            p.setLevel(level);
            String result = ClientProcessor.send(
                    IP, Port, getCommType(),
                    new Supervision("SUPERVISION.List", id, p));

            res = result.split(";");
        } catch (Exception ex) {
            res = null;
        }
        return res;
    }

    public static String sendEcho(String echo) throws Exception {
        String id = "ECHO10";
        ParamsEcho p = new ParamsEcho(echo);
        String result = ClientProcessor.send(
                IP, Port, getCommType(),
                new Echo("ECHO.Echo", id, p));

        return result;
    }

    public static String sendCommand(String device, String cmd, String timeout) throws Exception {
        String id = "CMD42";
        ParamsCmd p = new ParamsCmd(cmd, device, timeout);

        String result = ClientProcessor.send(
                IP, Port, getCommType(),
                new Cmd("CMD.SendCommand", id, p));

        return result;
    }

    // 
    // ----------------------------------------------------------------------------
    // 
    public static String getStatus(String device) throws Exception {
        String id = "CMD54";
        ParamsCmd p = new ParamsCmd();
        p.setDevice(device);

        String result = ClientProcessor.send(
                IP, Port, getCommType(),
                new Cmd("CMD.GetStatus", id, p));

        return result;
    }

    public static String truncate(String value, int maxLength) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

}
