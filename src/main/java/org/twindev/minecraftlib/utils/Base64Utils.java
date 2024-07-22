package org.twindev.minecraftlib.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Base64Utils {

    public static String encode(@NotNull final List<String> input) {
        if (input.isEmpty()) return null;
        String result = null;

        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(stream);

            final int size = input.size();
            out.writeInt(size);
            for (final String s : input) out.writeUTF(s);
            result = Base64.getEncoder().encodeToString(stream.toByteArray());

            out.close();
            stream.close();
        }

        catch (IOException e) { e.printStackTrace(); }
        return result;
    }

    public static List<String> decode(final String encoded) {
        final List<String> result = new ArrayList<>();
        if (encoded == null || encoded.length() < 1) return result;

        try {
            final byte[] data = Base64.getDecoder().decode(encoded);
            final ByteArrayInputStream stream = new ByteArrayInputStream(data);
            final DataInputStream in = new DataInputStream(stream);

            final int size = in.readInt();
            for (int i = 0; i < size; i++) result.add(in.readUTF());

            in.close();
            stream.close();
        }

        catch (IOException e) { e.printStackTrace(); }
        return result;
    }

}
