package top.theillusivec4.champions.integration.probejs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;

public class ChampionsProbeJSPlugin extends ProbeJSPlugin {
  public static ChampionsProbeJSPlugin create() {
    return new ChampionsProbeJSPlugin();
  }

  @Override
  public void addGlobals(ScriptDump scriptDump) {
    super.addGlobals(scriptDump);
  }
}
