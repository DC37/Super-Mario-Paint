package me.dc37.smp.interactors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import gui.loaders.LoaderBase;

public class LoaderWorker<M, L extends LoaderBase<M>> {

	private L loader;
	private FutureTask<M> future;
	private Thread thread;
	
	public LoaderWorker(L loader) {
		this.loader = loader;
		future = new FutureTask<>(this.loader);
		thread = new Thread(future);
	}
	
	public void start() {
		thread.start();
	}
	
	public double getLoadStatus() {
		return loader.getLoadStatus();
	}
	
	public boolean isAlive() {
		return thread.isAlive();
	}
	
	public M getResult() throws ExecutionException, InterruptedException {
		return future.get();
	}
	
}
