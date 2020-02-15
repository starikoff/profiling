package com.ratherabstract.timing.swatch;

import org.junit.Assert;
import org.junit.Test;

import static com.ratherabstract.timing.swatch_fmt.StopwatchFmtUtils.msd;

public class StopwatchesTest {

	private static int MS = 100;

	@Test
	public void test1() throws InterruptedException {
		RealStopwatch swatch = Stopwatches.install();

		swatch.start("node1");
		Thread.sleep(MS);
		swatch.stop();

		swatch.complete();

		Assert.assertEquals(1, swatch.root.children.size());
		StopwatchNode next = swatch.root.children.values().iterator().next();
		Assert.assertEquals("node1", next.tag);
		Assert.assertEquals(1, next.invocations);
		Assert.assertEquals(MS, msd(next.durationInnerNS), 10);
	}

	@Test
	public void test2() throws InterruptedException {
		RealStopwatch swatch = Stopwatches.install();

		swatch.start("node1");
		Thread.sleep(MS);
		swatch.stop();

		swatch.start("node1");
		Thread.sleep(MS);
		swatch.stop();

		swatch.complete();

		Assert.assertEquals(1, swatch.root.children.size());
		StopwatchNode next = swatch.root.children.values().iterator().next();
		Assert.assertEquals("node1", next.tag);
		Assert.assertEquals(2, next.invocations);
		Assert.assertEquals(2 * MS, msd(next.durationInnerNS), 10);
	}

	@Test
	public void test2different() throws InterruptedException {
		RealStopwatch swatch = Stopwatches.install();

		swatch.start("node1");
		Thread.sleep(MS);
		swatch.stop();

		swatch.start("node2");
		Thread.sleep(MS);
		swatch.stop();

		swatch.complete();

		Assert.assertEquals(2, swatch.root.children.size());
		StopwatchNode node1 = swatch.root.children.get("node1");
		StopwatchNode node2 = swatch.root.children.get("node2");
		Assert.assertEquals("node1", node1.tag);
		Assert.assertEquals("node2", node2.tag);
		Assert.assertEquals(1, node1.invocations);
		Assert.assertEquals(1, node2.invocations);
		Assert.assertEquals(MS, msd(node1.durationInnerNS), 10);
		Assert.assertEquals(MS, msd(node2.durationInnerNS), 10);
	}

}
