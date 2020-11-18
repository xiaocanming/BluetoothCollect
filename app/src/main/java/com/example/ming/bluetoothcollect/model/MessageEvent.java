package com.example.ming.bluetoothcollect.model;


public class MessageEvent {
    public enum MsgType {
        TimeBack,
        BatteryBack,
        ReceiveBack,
        RealData,
        HistoryData,
        ErrorData
    }

    private MsgType Type;

    private byte[] Message;


    public MessageEvent(MsgType type, byte[] msg) {
        Type=type;
        Message=msg;
    }

    public MsgType getType() {
        return Type;
    }

    public void setType(MsgType type) {
        Type = type;
    }

    public byte[] getMessage() {
        return Message;
    }

    public void setMessage(byte[] message) {
        Message = message;
    }
}
