package me.jishuna.betterpistons.nms.v1_20_R1;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.jishuna.betterpistons.BetterPistons;
import me.jishuna.betterpistons.nms.v1_20_R1.writer.PistonBaseClassWriter;
import me.jishuna.betterpistons.nms.v1_20_R1.writer.PistonBlockEntityClassWriter;
import me.jishuna.betterpistons.nms.v1_20_R1.writer.PistonStructureResolverClassWriter;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.piston.PistonStructureResolver;

public class NMSAdapter implements me.jishuna.betterpistons.nms.NMSAdapter {
    private static final String PISTON_BASE_CLASS = PistonBaseBlock.class.getCanonicalName().replace('.', '/');
    private static final String PISTON_BLOCK_ENTITY_CLASS = PistonMovingBlockEntity.class.getCanonicalName().replace('.', '/');
    private static final String PISTON_STRUCTURE_RESOLVER_CLASS = PistonStructureResolver.class.getCanonicalName().replace('.', '/');

    @Override
    public void onLoad(BetterPistons plugin) {
        instrumentClasses(plugin);
    }

    private void instrumentClasses(Plugin plugin) {
        plugin.getLogger().info("Attempting to transform minecraft classes.");
        try {
            Instrumentation instrumentation = ByteBuddyAgent.install();
            instrumentation.addTransformer(new InjectBootstrapTransformer(instrumentation, new File(plugin.getDataFolder(), "bootstrap")), true);

            instrumentation.addTransformer(new ClassFileTransformer() {

                @Override
                public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                    if (className.equals(PISTON_BASE_CLASS)) {
                        plugin.getLogger().log(Level.INFO, "Transforming {0}", PISTON_BASE_CLASS);
                        PistonBaseClassWriter cr = new PistonBaseClassWriter(classfileBuffer);
                        return cr.rewrite();
                    } else if (className.equals(PISTON_BLOCK_ENTITY_CLASS)) {
                        plugin.getLogger().log(Level.INFO, "Transforming {0}", PISTON_BLOCK_ENTITY_CLASS);
                        PistonBlockEntityClassWriter cr = new PistonBlockEntityClassWriter(classfileBuffer);
                        return cr.rewrite();
                    } else if (className.equals(PISTON_STRUCTURE_RESOLVER_CLASS)) {
                        plugin.getLogger().log(Level.INFO, "Transforming {0}", PISTON_STRUCTURE_RESOLVER_CLASS);
                        PistonStructureResolverClassWriter cr = new PistonStructureResolverClassWriter(classfileBuffer);
                        return cr.rewrite();
                    }
                    return classfileBuffer;
                }

            }, true);

            instrumentation.retransformClasses(Bootstraper.class, PistonStructureResolver.class, PistonBaseBlock.class, PistonMovingBlockEntity.class);

            plugin.getLogger().info("Transformation successful!");
        } catch (Exception ex) {
            ex.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "A fatal error occured while transforming minecraft classes: {0}", ex.getMessage());
            plugin.getLogger().severe("Plugin will now be disabled");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }
}