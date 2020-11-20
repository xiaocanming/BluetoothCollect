package com.example.ming.bluetoothcollect.util;

import android.content.Context;

import com.example.ming.bluetoothcollect.model.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AnalysisTool {
    private List<byte[]> buffer;
    private int buffernum;
    private EventStatus Status;
    private Lock lock;

    private enum EventStatus {
        Start,
        ReceiveReal,
        ReceiveHistory,
    }

    public AnalysisTool(Context context) {
        if (context == null) {
            throw new NullPointerException("Context null");
        }
        buffer = new ArrayList<>();
        Status = EventStatus.Start;
        lock = new ReentrantLock();
        buffernum = 0;
    }

    public MessageEvent TryParse(byte[] msg) {
        lock.lock();
        try {
            switch (this.Status) {
                case Start:
                    return TryStart(msg);
                case ReceiveReal:
                    return TryReceive(msg, MessageEvent.MsgType.RealData);
                case ReceiveHistory:
                    return TryReceive(msg, MessageEvent.MsgType.HistoryData);
                default:
                    Clear();
                    return new MessageEvent(MessageEvent.MsgType.ErrorData, msg);
            }
        } catch (Exception e) {
            return new MessageEvent(MessageEvent.MsgType.ErrorData, msg);
        } finally {
            lock.unlock();
        }
    }


    private MessageEvent TryStart(byte[] msg) {
        if (isStart(msg)) {
            buffer.add(msg);
            buffernum = buffernum + msg.length;
            return new MessageEvent(MessageEvent.MsgType.ReceiveBack, msg);
        } else if (msg[0] == 0x03 && msg[7] == 0x07) {
            return new MessageEvent(MessageEvent.MsgType.TimeBack, msg);
        } else if (msg[0] == 0x05 && msg.length==3) {
            return new MessageEvent(MessageEvent.MsgType.BatteryBack, msg);
        } else {
            return new MessageEvent(MessageEvent.MsgType.ErrorData, msg);
        }
    }

    private MessageEvent TryReceive(byte[] msg, MessageEvent.MsgType MsgType) {
        buffer.add(msg);
        buffernum = buffernum + msg.length;
        if (isEnd(msg)) {
            byte returnmsg[] = new byte[buffernum];
            int i = 0;
            for (byte[] b : buffer
            ) {
                for (byte singleb : b
                ) {
                    returnmsg[i] = singleb;
                    i++;
                }
            }
            Clear();
            this.Status = EventStatus.Start;
            return new MessageEvent(MsgType, returnmsg);
        } else if (isStart(msg)) {
            buffer.add(msg);
            buffernum = buffernum + msg.length;
            return new MessageEvent(MessageEvent.MsgType.ReceiveBack, msg);
        } else {
            return new MessageEvent(MessageEvent.MsgType.ReceiveBack, msg);
        }
    }

    private boolean isStart(byte[] msg) {
        if (msg.length == 8) {
            if (msg[0] == (byte) 0xEE && msg[1] == (byte) 0xEE && msg[2] == (byte) 0xEE && msg[3] == (byte) 0xEE && msg[4] == (byte) 0xEE && msg[5] == (byte) 0xEE && msg[6] == (byte) 0xEE && msg[7] == (byte) 0xEE) {
                Clear();
                this.Status = EventStatus.ReceiveHistory;
                return true;
            }
            if (msg[6] == (byte) 0xEB && msg[7] == (byte) 0X90) {
                Clear();
                this.Status = EventStatus.ReceiveReal;
                return true;
            }
        }
        return false;
    }

    private boolean isEnd(byte[] msg) {
        if (msg.length == 8) {
            if (msg[0] == (byte) 0xFF && msg[1] == (byte) 0xFF && msg[2] == (byte) 0xFF && msg[3] == (byte) 0xFF && msg[4] == (byte) 0xFF && msg[5] == (byte) 0xFF && msg[6] == (byte) 0xFF && msg[7] == (byte) 0xFF) {
                return true;
            }
        }
        return false;
    }

    private void Clear() {
        buffer.clear();
        buffernum = 0;
    }

}
