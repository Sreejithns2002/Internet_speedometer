package com.sreejithsreejayan.internetspeedometer;

public class Speed {
    private long uploadSpeed=0;
    private long downloadSpeed=0;
    private long totalSpeed=0;

    NetSpeed nUpSpeed= new NetSpeed();
    NetSpeed nDownSpeed = new NetSpeed();
    NetSpeed nTotalSpeed = new NetSpeed();

    boolean isItBit=false;

    public void calculateSpeed(long uploadBytes,long downloadBytes, long usageTime){
        long totalBytes=uploadBytes+downloadBytes;
        if(usageTime>0){
            totalSpeed=totalBytes*1000/usageTime;
            uploadSpeed=uploadBytes*1000/usageTime;
            downloadSpeed=downloadBytes*1000/usageTime;
            updateNetSpeed();
        }

    }

    private void updateNetSpeed() {
        nUpSpeed.setNetSpeed(uploadSpeed);
        nDownSpeed.setNetSpeed(downloadSpeed);
        nTotalSpeed.setNetSpeed(totalSpeed);
    }

    NetSpeed getSpeed(String Speed){
        switch (Speed){
            case "upSpeed":
                return nUpSpeed;
            case "downSpeed":
                return nDownSpeed;
            default:
                return nTotalSpeed;
        }
    }

    class NetSpeed{
        String nSpeed;
        String nSpeedUnit;

        private void setNetSpeed(long speed){
            if (isItBit){
                speed *= 8;
            }
            if (speed< 1024L){
                nSpeed=Long.toString(speed);
                if (isItBit){
                    nSpeedUnit="B/s";
                }else {
                    nSpeedUnit="b/s";
                }
            }else if (speed< 1048576L){
                nSpeed=Long.toString(speed/1024L);
                if (isItBit){
                    nSpeedUnit="kB/s";
                }else {
                    nSpeedUnit="kb/s";
                }
            }else if (speed< 1073741824L){
                nSpeed=Long.toString(speed/1048576L);
                if (isItBit){
                    nSpeedUnit="MB/s";
                }else {
                    nSpeedUnit = "Mb/s";
                }   }else if (speed< 1099511627776L){
                nSpeed=Long.toString(speed/1073741824L);
                    if (isItBit){
                        nSpeedUnit="GB/s";
                    }else {
                        nSpeedUnit = "Gb/s";
                    }
                    }else{
                nSpeed=Long.toString(speed/1099511627776L);
                        if (isItBit){
                            nSpeedUnit="TB/s";
                        }else {
                            nSpeedUnit="Tb/s";
                        }
        }
    }}



}
