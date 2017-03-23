package com.graal.utils;

import com.junitsupport.TestSetup;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by KanthKumar on 3/20/17.
 */
public class GraphletsUtilTest extends TestSetup{
    private GraphletsUtil graphletsUtil = ImmutableGraphletsUtil.of();

    @Test
    public void getGraphletsTest() {
        int uniqueGraphlets = graphletsUtil.getGraphlets().distinct().size();
        Assert.assertEquals(graphletsUtil.getGraphlets().size(), uniqueGraphlets);
    }

    @Test
    public void getOrbitVerticesTest() {
        int uniqueOrbitVertices = graphletsUtil.getOrbitVertices().distinct().size();
        Assert.assertEquals(graphletsUtil.getOrbitVertices().size(), uniqueOrbitVertices);
    }

    @Test
    public void getOrbitsWeightTest() {
        int size = graphletsUtil.getOrbitsWeight().distinct().size();
        Assert.assertEquals(9, size);
    }

    @Test
    public void getTotalOrbitsWeightTest() {
        double weight = graphletsUtil.getTotalOrbitsWeight();
        Assert.assertEquals(45.1483349474, weight, 0.0000000001);
    }
}
