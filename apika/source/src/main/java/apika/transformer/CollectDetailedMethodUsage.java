package apika.transformer;

import soot.Scene;
import soot.SceneTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by guoxing on 26/5/2017.
 */
public class CollectDetailedMethodUsage extends SceneTransformer {
    @Override
    protected void internalTransform(String s, Map<String, String> map) {
        CallGraph cg = Scene.v().getCallGraph();

        System.out.println("edges count: " + cg.size());
        for (Iterator<Edge> i = cg.iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
    }
}
