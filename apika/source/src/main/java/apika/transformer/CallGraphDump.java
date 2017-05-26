package apika.transformer;

import soot.Scene;
import soot.SceneTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.Map;

/**
 * Created by guoxing on 26/5/2017.
 */
public class CallGraphDump extends SceneTransformer {

    @Override
    protected void internalTransform(String s, Map<String, String> map) {
        CallGraph cg = Scene.v().getCallGraph();
        cg.forEach(System.out::println);
        System.out.println("call graph size = " + cg.size());
    }
}
