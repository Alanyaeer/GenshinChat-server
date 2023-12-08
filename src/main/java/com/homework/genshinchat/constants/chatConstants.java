package com.homework.genshinchat.constants;

/**
 * @author 吴嘉豪
 * @date 2023/12/7 20:51
 */
public class chatConstants {
    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"myId\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"friendId\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"uid\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"msg\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"time\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"chatType\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"extend\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"size\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"fileName\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"headImg\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"fileType\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"imgType\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
