package net.contra.obfuscator.trans.ob;

import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.*;
import net.contra.obfuscator.ITransformer;
import net.contra.obfuscator.ObfuscationType;
import net.contra.obfuscator.Settings;
import net.contra.obfuscator.util.bcel.BCELMethods;
import net.contra.obfuscator.util.bcel.JarLoader;
import net.contra.obfuscator.util.misc.LogHandler;


public class IntegerComplicator implements ITransformer {
    private final LogHandler Logger = new LogHandler("AttributeObfuscator");
    private String Location = "";
    private JarLoader LoadedJar;

    public IntegerComplicator(String loc) {
        Location = loc;
    }

    public void load() {
        LoadedJar = new JarLoader(Location);
    }

    public void transform() {
        for (ClassGen cg : LoadedJar.ClassEntries.values()) {
            for (Method method : cg.getMethods()) {
                MethodGen mg = new MethodGen(method, cg.getClassName(), cg.getConstantPool());
                InstructionList list = mg.getInstructionList();
                if (list == null) continue;
                Logger.log("Complicating Constant Integers -> Class: " + cg.getClassName() + " Method: " + method.getName());
                InstructionHandle[] handles = list.getInstructionHandles();
                for (InstructionHandle handle : handles) {
                    if (handle.getPosition() >= handles.length - 1) continue;

                    if (Settings.OBFUSCATION_LEVEL.getLevel() < ObfuscationType.Normal.getLevel()) {
                        //If we have it on normal or light, we won't obfuscate boolean values :)
                        if (BCELMethods.isFieldInvoke(handle.getNext().getInstruction())) {
                            String sig = BCELMethods.getFieldInvokeSignature(handle.getNext().getInstruction(), cg.getConstantPool());
                            if (sig.trim().startsWith("Z")) continue;
                        }
                        if (handle.getNext().getInstruction() instanceof IRETURN) {
                            if (mg.getSignature().trim().endsWith("Z")) continue;
                        }
                    }
                    if (handle.getInstruction() instanceof ICONST
                            || handle.getInstruction() instanceof BIPUSH
                            || handle.getInstruction() instanceof SIPUSH
                            || handle.getInstruction() instanceof ILOAD
                            || (handle.getInstruction() instanceof LDC && handle.getNext().getInstruction() instanceof IASTORE)) {
                        InstructionList nlist = new InstructionList();
                        for (int i = 0; i < Settings.ITERATIONS; i++) {
                            nlist.append(new ICONST(0));
                            nlist.append(new IADD());
                            if (Settings.OBFUSCATION_LEVEL.getLevel() > ObfuscationType.Normal.getLevel()) {
                                nlist.append(new ICONST(1));
                                nlist.append(new IMUL());
                                nlist.append(new ICONST(1));
                                nlist.append(new IMUL());
                            }
                        }
                        if (Settings.OBFUSCATION_LEVEL.getLevel() > ObfuscationType.Normal.getLevel()
                                && handle.getPrev() != null) {
                            InstructionList prelist = new InstructionList();
                            prelist.append(new ICONST(0));
                            for (int i = 0; i < Settings.ITERATIONS; i++) {
                                prelist.append(new ICONST(0));
                                prelist.append(new IADD());
                            }
                            list.insert(handle, prelist);
                            nlist.insert(new IADD());
                        }
                        list.append(handle, nlist);
                    }
                }
                list.setPositions();
                mg.setInstructionList(list);
                mg.setMaxLocals();
                mg.setMaxStack();
                cg.replaceMethod(method, mg.getMethod());
            }
        }
    }

    public void save() {
        String loc = Location.replace(".jar", Settings.FILE_TAG + ".jar");
        LoadedJar.saveJar(loc);

    }
}

