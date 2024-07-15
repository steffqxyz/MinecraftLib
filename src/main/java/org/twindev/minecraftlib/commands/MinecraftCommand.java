package org.twindev.minecraftlib.commands;

import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.model.Tuple;
import org.twindev.minecraftlib.commands.annotations.Command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MinecraftCommand extends SimpleCommand {
    private final Map<String, Tuple<Method, Command>> methods = new HashMap<>();

    public MinecraftCommand(String label) {
        super(label);

        this.findMethods();
    }

    private void findMethods() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command parameter = method.getDeclaredAnnotation(Command.class);
                int modifiers = method.getModifiers();

                Valid.checkBoolean(!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers),
                        "Method " + method.getName() + " in class " + this.getClass().getSimpleName() + " must be public and non-static!");

                this.methods.put(parameter.name(), new Tuple<>(method, parameter));
            }
        }
    }

    @Override
    protected final void onCommand() {
        String param = this.args.length > 0 ? this.args[0] : "";
        Tuple<Method, Command> methodTuple = this.methods.get(param.toLowerCase());

        if (methodTuple != null) {
            Method method = methodTuple.getKey();
            Command parameter = methodTuple.getValue();

            if (parameter.requiresPlayer() && !this.isPlayer())
                returnTell("This command requires a player!");

            ReflectionUtil.invoke(method, this);
        } else
            this.tell("Usage: /{label} <" + String.join("|", this.methods.keySet()
                    .stream().filter(methodName -> !methodName.isEmpty()).collect(Collectors.toList())) + ">");
    }

    @Override
    protected List<String> tabComplete() {

        if (this.args.length == 1)
            return this.completeLastWord(this.methods.keySet());

        return NO_COMPLETE;
    }
}