package no.phasfjo;

public class Processing {

    ProcessHandle.Info info;

    public ProcessHandle.Info getInfo() {
        return info;
    }

    public void setInfo(ProcessHandle.Info info) {
        this.info = info;
    }

    public Processing() {
    }

    public static void printInfo(ProcessHandle.Info info){
        if(info.startInstant().isPresent() && info.command().isPresent()) {
            System.out.println("Started at: " + info.startInstant().get() +
                    ", Command: " + info.command().get());
        }
    }

}
