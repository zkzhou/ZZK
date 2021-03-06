package myframelib.zkzhou.com.common.taskscheduler.schedule;

import myframelib.zkzhou.com.common.taskscheduler.callback.Function;

/**
 * FunctionEmitter
 * Created by D on 2018/5/16.
 */
public class FunctionEmitter<T, R> extends Emitter {
    public Function<? super T, ? extends R> function;

    public FunctionEmitter(Function<? super T, ? extends R> function, @Schedulers.Scheduler int scheduler) {
        this.function = function;
        this.scheduler = scheduler;
    }
}
