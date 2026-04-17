package top.theillusivec4.champions.integration.probejs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;
import top.theillusivec4.champions.integration.probejs.docs.LevelBasedValueDocs;

public class ChampionsProbeJSPlugin extends ProbeJSPlugin {

  public static ChampionsProbeJSPlugin create() {
    return new ChampionsProbeJSPlugin();
  }

  @Override
  public void assignType(ScriptDump scriptDump) {
    LevelBasedValueDocs.assignType(scriptDump);
  }
}
