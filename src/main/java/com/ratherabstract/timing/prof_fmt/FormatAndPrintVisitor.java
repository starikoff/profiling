package com.ratherabstract.timing.prof_fmt;

import com.ratherabstract.timing.swatch.StopwatchNode;
import com.ratherabstract.timing.prof.StopwatchVisitor;

import java.util.function.Consumer;

public class FormatAndPrintVisitor implements StopwatchVisitor {

	private StopwatchFormatter formatter;

	private Consumer<String> printer;

	public FormatAndPrintVisitor(StopwatchFormatter formatter, Consumer<String> printer) {
		this.formatter = formatter;
		this.printer = printer;
	}

	@Override
	public void enteredRoot(StopwatchNode root) {
		formatter.enteredRoot(root);
	}

	@Override
	public void enteredNode(StopwatchNode node) {
		formatter.enteredNode(node);
	}

	@Override
	public void exitedNode(StopwatchNode node) {
		formatter.exitedNode(node);
	}

	@Override
	public void exitedRoot(StopwatchNode root) {
		formatter.exitedRoot(root);
		String result = formatter.get();

		printer.accept(result);
	}
}
