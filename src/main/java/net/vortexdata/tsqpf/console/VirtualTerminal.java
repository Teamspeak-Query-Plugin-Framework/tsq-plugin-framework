package net.vortexdata.tsqpf.console;

/**
 * @deprecated Use {@link IShell} instead!
 */
@Deprecated
public interface VirtualTerminal {

    void println(String msg);

    void print(String msg);

    String readln();


}
