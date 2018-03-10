package edu.fdu.se.astdiff.linkpool;

import com.github.javaparser.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/9.
 *
 */
public class ControlFlow {
    public List<String> controlFlowList;

    public ControlFlow(){
        controlFlowList = new ArrayList<>();
    }

    private Range controlFlowRange;
}
