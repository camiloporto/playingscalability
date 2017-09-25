package br.com.camiloporto.scalability.tasks;

/**
 * Created by camiloporto on 9/10/17.
 */
public interface Task {

    String ID_CPU_INTENSIVE_TASK = "t1";
    String ID_IO_INTENSIVE_TASK = "t2";

    byte[] execute();

}
