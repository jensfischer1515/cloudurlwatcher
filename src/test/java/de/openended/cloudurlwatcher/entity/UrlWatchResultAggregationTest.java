package de.openended.cloudurlwatcher.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UrlWatchResultAggregationTest {

    private static final String URL = "url";

    private UrlWatchResultAggregation urlWatchResultAggregation;

    @Before
    public void onSetup() {
        urlWatchResultAggregation = new UrlWatchResultAggregation(URL);
        assertEquals("url", URL, urlWatchResultAggregation.getUrl());
        assertEquals("aggregateCount", 0L, urlWatchResultAggregation.getAggregateCount());
        assertEquals("minTimestamp", Long.MAX_VALUE, urlWatchResultAggregation.getMinTimestamp());
        assertEquals("maxTimestamp", Long.MIN_VALUE, urlWatchResultAggregation.getMaxTimestamp());
        assertTrue("statusCodeCounts", urlWatchResultAggregation.getStatusCodeCounts().isEmpty());
        assertTrue("minResponseTimes", urlWatchResultAggregation.getMinResponseTimes().isEmpty());
        assertTrue("maxResponseTimes", urlWatchResultAggregation.getMaxResponseTimes().isEmpty());
        assertTrue("avgResponseTimes", urlWatchResultAggregation.getAvgResponseTimes().isEmpty());
    }

    @Test
    public void testAddOne() {
        UrlWatchResult urlWatchResult = new UrlWatchResult(URL);
        urlWatchResult.setTimestamp(1111111);
        urlWatchResult.setStatusCode(200);
        urlWatchResult.setResponseTimeMillis(100);

        urlWatchResultAggregation.aggregateUrlWatchResult(urlWatchResult);
        assertEquals("url", URL, urlWatchResultAggregation.getUrl());
        assertEquals("aggregateCount", 1, urlWatchResultAggregation.getAggregateCount());
        assertEquals("minTimestamp", 1111111, urlWatchResultAggregation.getMinTimestamp(), 0.0);
        assertEquals("maxTimestamp", 1111111, urlWatchResultAggregation.getMaxTimestamp(), 0.0);
        assertEquals("statusCodeCounts", 200, urlWatchResultAggregation.getStatusCodeCounts().get(0).getStatusCode());
        assertEquals("statusCodeCounts", 1L, urlWatchResultAggregation.getStatusCodeCounts().get(0).getCount());
        assertEquals("minResponseTimes", new Long(100L), urlWatchResultAggregation.getMinResponseTimes().get(200));
        assertEquals("maxResponseTimes", new Long(100L), urlWatchResultAggregation.getMaxResponseTimes().get(200));
        assertEquals("avgResponseTimes", new Double(100.0), urlWatchResultAggregation.getAvgResponseTimes().get(200), 0.0);
    }

    @Test
    public void testAddTwo() {
        testAddOne();

        UrlWatchResult urlWatchResult = new UrlWatchResult(URL);
        urlWatchResult.setTimestamp(2222222);
        urlWatchResult.setStatusCode(200);
        urlWatchResult.setResponseTimeMillis(150);

        urlWatchResultAggregation.aggregateUrlWatchResult(urlWatchResult);
        assertEquals("url", URL, urlWatchResultAggregation.getUrl());
        assertEquals("aggregateCount", 2, urlWatchResultAggregation.getAggregateCount());
        assertEquals("minTimestamp", 1111111, urlWatchResultAggregation.getMinTimestamp(), 0.0);
        assertEquals("maxTimestamp", 2222222, urlWatchResultAggregation.getMaxTimestamp(), 0.0);
        assertEquals("statusCodeCounts", 200, urlWatchResultAggregation.getStatusCodeCounts().get(0).getStatusCode());
        assertEquals("statusCodeCounts", 2L, urlWatchResultAggregation.getStatusCodeCounts().get(0).getCount());
        assertEquals("minResponseTimes", new Long(100L), urlWatchResultAggregation.getMinResponseTimes().get(200));
        assertEquals("maxResponseTimes", new Long(150L), urlWatchResultAggregation.getMaxResponseTimes().get(200));
        assertEquals("avgResponseTimes", new Double(125.0), urlWatchResultAggregation.getAvgResponseTimes().get(200), 0.0);
    }

    @Test
    public void testAddThree() {
        testAddTwo();

        UrlWatchResult urlWatchResult = new UrlWatchResult(URL);
        urlWatchResult.setTimestamp(3333333);
        urlWatchResult.setStatusCode(200);
        urlWatchResult.setResponseTimeMillis(200);

        urlWatchResultAggregation.aggregateUrlWatchResult(urlWatchResult);
        assertEquals("url", URL, urlWatchResultAggregation.getUrl());
        assertEquals("aggregateCount", 3, urlWatchResultAggregation.getAggregateCount());
        assertEquals("minTimestamp", 1111111, urlWatchResultAggregation.getMinTimestamp(), 0.0);
        assertEquals("maxTimestamp", 3333333, urlWatchResultAggregation.getMaxTimestamp(), 0.0);
        assertEquals("statusCodeCounts", 200, urlWatchResultAggregation.getStatusCodeCounts().get(0).getStatusCode());
        assertEquals("statusCodeCounts", 3L, urlWatchResultAggregation.getStatusCodeCounts().get(0).getCount());
        assertEquals("minResponseTimes", new Long(100L), urlWatchResultAggregation.getMinResponseTimes().get(200));
        assertEquals("maxResponseTimes", new Long(200L), urlWatchResultAggregation.getMaxResponseTimes().get(200));
        assertEquals("avgResponseTimes", new Double(150.0), urlWatchResultAggregation.getAvgResponseTimes().get(200), 0.0);
    }
}
