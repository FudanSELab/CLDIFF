import com.google.gson.Gson;
import edu.fdu.se.main.astdiff.FileUtil;
import edu.fdu.se.main.astdiff.Meta;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    static final String response = "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/server/RequestHandler.java\"\n" +
            "\n" +
            "/*\n" +
            " *\n" +
            " *  *    Copyright (C) 2016 Amit Shekhar\n" +
            " *  *    Copyright (C) 2011 Android Open Source Project\n" +
            " *  *\n" +
            " *  *    Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " *  *    you may not use this file except in compliance with the License.\n" +
            " *  *    You may obtain a copy of the License at\n" +
            " *  *\n" +
            " *  *        http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *  *\n" +
            " *  *    Unless required by applicable law or agreed to in writing, software\n" +
            " *  *    distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " *  *    See the License for the specific language governing permissions and\n" +
            " *  *    limitations under the License.\n" +
            " *\n" +
            " */\n" +
            "\n" +
            "package com.amitshekhar.server;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "import android.content.res.AssetManager;\n" +
            "import android.net.Uri;\n" +
            "import android.text.TextUtils;\n" +
            "import android.util.Pair;\n" +
            "\n" +
            "import com.amitshekhar.model.Response;\n" +
            "import com.amitshekhar.model.RowDataRequest;\n" +
            "import com.amitshekhar.model.TableDataResponse;\n" +
            "import com.amitshekhar.model.UpdateRowResponse;\n" +
            "import com.amitshekhar.sqlite.DebugSQLiteDB;\n" +
            "import com.amitshekhar.sqlite.SQLiteDB;\n" +
            "import com.amitshekhar.utils.Constants;\n" +
            "import com.amitshekhar.utils.DatabaseFileProvider;\n" +
            "import com.amitshekhar.utils.DatabaseHelper;\n" +
            "import com.amitshekhar.utils.PrefHelper;\n" +
            "import com.amitshekhar.utils.Utils;\n" +
            "import com.google.gson.Gson;\n" +
            "import com.google.gson.GsonBuilder;\n" +
            "import com.google.gson.reflect.TypeToken;\n" +
            "\n" +
            "import net.sqlcipher.database.SQLiteDatabase;\n" +
            "\n" +
            "import java.io.BufferedReader;\n" +
            "import java.io.File;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStreamReader;\n" +
            "import java.io.PrintStream;\n" +
            "import java.net.Socket;\n" +
            "import java.net.URLDecoder;\n" +
            "import java.util.HashMap;\n" +
            "import java.util.List;\n" +
            "\n" +
            "/**\n" +
            " * Created by amitshekhar on 06/02/17.\n" +
            " */\n" +
            "\n" +
            "public class RequestHandler {\n" +
            "\n" +
            "    private final Context mContext;\n" +
            "    private final Gson mGson;\n" +
            "    private final AssetManager mAssets;\n" +
            "    private boolean isDbOpened;\n" +
            "    private SQLiteDB sqLiteDB;\n" +
            "    private HashMap> mDatabaseFiles;\n" +
            "    private HashMap> mCustomDatabaseFiles;\n" +
            "    private String mSelectedDatabase = null;\n" +
            "\n" +
            "    public RequestHandler(Context context) {\n" +
            "        mContext = context;\n" +
            "        mAssets = context.getResources().getAssets();\n" +
            "        mGson = new GsonBuilder().serializeNulls().create();\n" +
            "    }\n" +
            "\n" +
            "    public void handle(Socket socket) throws IOException {\n" +
            "        BufferedReader reader = null;\n" +
            "        PrintStream output = null;\n" +
            "        try {\n" +
            "            String route = null;\n" +
            "\n" +
            "            // Read HTTP headers and parse out the route.\n" +
            "            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));\n" +
            "            String line;\n" +
            "            while (!TextUtils.isEmpty(line = reader.readLine())) {\n" +
            "                if (line.startsWith(\"GET /\")) {\n" +
            "                    int start = line.indexOf('/') + 1;\n" +
            "                    int end = line.indexOf(' ', start);\n" +
            "                    route = line.substring(start, end);\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            // Output stream that we send the response to\n" +
            "            output = new PrintStream(socket.getOutputStream());\n" +
            "\n" +
            "            if (route == null || route.isEmpty()) {\n" +
            "                route = \"index.html\";\n" +
            "            }\n" +
            "\n" +
            "            byte[] bytes;\n" +
            "\n" +
            "            if (route.startsWith(\"getDbList\")) {\n" +
            "                final String response = getDBListResponse();\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"getAllDataFromTheTable\")) {\n" +
            "                final String response = getAllDataFromTheTableResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"getTableList\")) {\n" +
            "                final String response = getTableListResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"addTableData\")) {\n" +
            "                final String response = addTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"updateTableData\")) {\n" +
            "                final String response = updateTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"deleteTableData\")) {\n" +
            "                final String response = deleteTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"query\")) {\n" +
            "                final String response = executeQueryAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"downloadDb\")) {\n" +
            "                bytes = Utils.getDatabase(mSelectedDatabase, mDatabaseFiles);\n" +
            "            } else {\n" +
            "                bytes = Utils.loadContent(route, mAssets);\n" +
            "            }\n" +
            "\n" +
            "            if (null == bytes) {\n" +
            "                writeServerError(output);\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            // Send out the content.\n" +
            "            output.println(\"HTTP/1.0 200 OK\");\n" +
            "            output.println(\"Content-Type: \" + Utils.detectMimeType(route));\n" +
            "\n" +
            "            if (route.startsWith(\"downloadDb\")) {\n" +
            "                output.println(\"Content-Disposition: attachment; filename=\" + mSelectedDatabase);\n" +
            "            } else {\n" +
            "                output.println(\"Content-Length: \" + bytes.length);\n" +
            "            }\n" +
            "            output.println();\n" +
            "            output.write(bytes);\n" +
            "            output.flush();\n" +
            "        } finally {\n" +
            "            try {\n" +
            "                if (null != output) {\n" +
            "                    output.close();\n" +
            "                }\n" +
            "                if (null != reader) {\n" +
            "                    reader.close();\n" +
            "                }\n" +
            "            } catch (Exception e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public void setCustomDatabaseFiles(HashMap> customDatabaseFiles) {\n" +
            "        mCustomDatabaseFiles = customDatabaseFiles;\n" +
            "    }\n" +
            "\n" +
            "    private void writeServerError(PrintStream output) {\n" +
            "        output.println(\"HTTP/1.0 500 Internal Server Error\");\n" +
            "        output.flush();\n" +
            "    }\n" +
            "\n" +
            "    private void openDatabase(String database) {\n" +
            "        closeDatabase();\n" +
            "        File databaseFile = mDatabaseFiles.get(database).first;\n" +
            "        String password = mDatabaseFiles.get(database).second;\n" +
            "\n" +
            "        SQLiteDatabase.loadLibs(mContext);\n" +
            "\n" +
            "        sqLiteDB = new DebugSQLiteDB(SQLiteDatabase.openOrCreateDatabase(databaseFile.getAbsolutePath(), password, null));\n" +
            "        isDbOpened = true;\n" +
            "    }\n" +
            "\n" +
            "    private void closeDatabase() {\n" +
            "        if (sqLiteDB != null && sqLiteDB.isOpen()) {\n" +
            "            sqLiteDB.close();\n" +
            "        }\n" +
            "        sqLiteDB = null;\n" +
            "        isDbOpened = false;\n" +
            "    }\n" +
            "\n" +
            "    private String getDBListResponse() {\n" +
            "        mDatabaseFiles = DatabaseFileProvider.getDatabaseFiles(mContext);\n" +
            "        if (mCustomDatabaseFiles != null) {\n" +
            "            mDatabaseFiles.putAll(mCustomDatabaseFiles);\n" +
            "        }\n" +
            "        Response response = new Response();\n" +
            "        if (mDatabaseFiles != null) {\n" +
            "            for (HashMap.Entry> entry : mDatabaseFiles.entrySet()) {\n" +
            "                String[] dbEntry = {entry.getKey(), !entry.getValue().second.equals(\"\") ? \"true\" : \"false\"};\n" +
            "                response.rows.add(dbEntry);\n" +
            "            }\n" +
            "        }\n" +
            "        response.rows.add(new String[]{Constants.APP_SHARED_PREFERENCES, \"false\"});\n" +
            "        response.isSuccessful = true;\n" +
            "        return mGson.toJson(response);\n" +
            "    }\n" +
            "\n" +
            "    private String getAllDataFromTheTableResponse(String route) {\n" +
            "\n" +
            "        String tableName = null;\n" +
            "\n" +
            "        if (route.contains(\"?tableName=\")) {\n" +
            "            tableName = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "        }\n" +
            "\n" +
            "        TableDataResponse response;\n" +
            "\n" +
            "        if (isDbOpened) {\n" +
            "            String sql = \"SELECT * FROM \" + tableName;\n" +
            "            response = DatabaseHelper.getTableData(sqLiteDB, sql, tableName);\n" +
            "        } else {\n" +
            "            response = PrefHelper.getAllPrefData(mContext, tableName);\n" +
            "        }\n" +
            "\n" +
            "        return mGson.toJson(response);\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    private String executeQueryAndGetResponse(String route) {\n" +
            "        String query = null;\n" +
            "        String data = null;\n" +
            "        String first;\n" +
            "        try {\n" +
            "            if (route.contains(\"?query=\")) {\n" +
            "                query = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "            }\n" +
            "            try {\n" +
            "                query = URLDecoder.decode(query, \"UTF-8\");\n" +
            "            } catch (Exception e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "\n" +
            "            if (query != null) {\n" +
            "                String[] statements = query.split(\";\");\n" +
            "\n" +
            "                for (int i = 0; i < statements.length; i++) {\n" +
            "\n" +
            "                    String aQuery = statements[i].trim();\n" +
            "                    first = aQuery.split(\" \")[0].toLowerCase();\n" +
            "                    if (first.equals(\"select\") || first.equals(\"pragma\")) {\n" +
            "                        TableDataResponse response = DatabaseHelper.getTableData(sqLiteDB, aQuery, null);\n" +
            "                        data = mGson.toJson(response);\n" +
            "                        if (!response.isSuccessful) {\n" +
            "                            break;\n" +
            "                        }\n" +
            "                    } else {\n" +
            "                        TableDataResponse response = DatabaseHelper.exec(sqLiteDB, aQuery);\n" +
            "                        data = mGson.toJson(response);\n" +
            "                        if (!response.isSuccessful) {\n" +
            "                            break;\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "\n" +
            "        if (data == null) {\n" +
            "            Response response = new Response();\n" +
            "            response.isSuccessful = false;\n" +
            "            data = mGson.toJson(response);\n" +
            "        }\n" +
            "\n" +
            "        return data;\n" +
            "    }\n" +
            "\n" +
            "    private String getTableListResponse(String route) {\n" +
            "        String database = null;\n" +
            "        if (route.contains(\"?database=\")) {\n" +
            "            database = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "        }\n" +
            "\n" +
            "        Response response;\n" +
            "\n" +
            "        if (Constants.APP_SHARED_PREFERENCES.equals(database)) {\n" +
            "            response = PrefHelper.getAllPrefTableName(mContext);\n" +
            "            closeDatabase();\n" +
            "            mSelectedDatabase = Constants.APP_SHARED_PREFERENCES;\n" +
            "        } else {\n" +
            "            openDatabase(database);\n" +
            "            response = DatabaseHelper.getAllTableName(sqLiteDB);\n" +
            "            mSelectedDatabase = database;\n" +
            "        }\n" +
            "        return mGson.toJson(response);\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private String addTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"addData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.addOrUpdateRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.addRow(sqLiteDB, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private String updateTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"updatedData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.addOrUpdateRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.updateRow(sqLiteDB, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private String deleteTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"deleteData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.deleteRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.deleteRow(sqLiteDB, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413/debug-db/src/main/java/com/amitshekhar/server/RequestHandler.java----parent0\"\n" +
            "\n" +
            "/*\n" +
            " *\n" +
            " *  *    Copyright (C) 2016 Amit Shekhar\n" +
            " *  *    Copyright (C) 2011 Android Open Source Project\n" +
            " *  *\n" +
            " *  *    Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " *  *    you may not use this file except in compliance with the License.\n" +
            " *  *    You may obtain a copy of the License at\n" +
            " *  *\n" +
            " *  *        http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *  *\n" +
            " *  *    Unless required by applicable law or agreed to in writing, software\n" +
            " *  *    distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " *  *    See the License for the specific language governing permissions and\n" +
            " *  *    limitations under the License.\n" +
            " *\n" +
            " */\n" +
            "\n" +
            "package com.amitshekhar.server;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "import android.content.res.AssetManager;\n" +
            "import android.net.Uri;\n" +
            "import android.text.TextUtils;\n" +
            "import android.util.Pair;\n" +
            "\n" +
            "import com.amitshekhar.model.Response;\n" +
            "import com.amitshekhar.model.RowDataRequest;\n" +
            "import com.amitshekhar.model.TableDataResponse;\n" +
            "import com.amitshekhar.model.UpdateRowResponse;\n" +
            "import com.amitshekhar.utils.Constants;\n" +
            "import com.amitshekhar.utils.DatabaseFileProvider;\n" +
            "import com.amitshekhar.utils.DatabaseHelper;\n" +
            "import com.amitshekhar.utils.PrefHelper;\n" +
            "import com.amitshekhar.utils.Utils;\n" +
            "import com.google.gson.Gson;\n" +
            "import com.google.gson.GsonBuilder;\n" +
            "import com.google.gson.reflect.TypeToken;\n" +
            "\n" +
            "import net.sqlcipher.database.SQLiteDatabase;\n" +
            "\n" +
            "import java.io.BufferedReader;\n" +
            "import java.io.File;\n" +
            "import java.io.IOException;\n" +
            "import java.io.InputStreamReader;\n" +
            "import java.io.PrintStream;\n" +
            "import java.net.Socket;\n" +
            "import java.net.URLDecoder;\n" +
            "import java.util.HashMap;\n" +
            "import java.util.List;\n" +
            "\n" +
            "/**\n" +
            " * Created by amitshekhar on 06/02/17.\n" +
            " */\n" +
            "\n" +
            "public class RequestHandler {\n" +
            "\n" +
            "    private final Context mContext;\n" +
            "    private final Gson mGson;\n" +
            "    private final AssetManager mAssets;\n" +
            "    private boolean isDbOpened;\n" +
            "    private SQLiteDatabase mDatabase;\n" +
            "    private HashMap> mDatabaseFiles;\n" +
            "    private HashMap> mCustomDatabaseFiles;\n" +
            "    private String mSelectedDatabase = null;\n" +
            "\n" +
            "    public RequestHandler(Context context) {\n" +
            "        mContext = context;\n" +
            "        mAssets = context.getResources().getAssets();\n" +
            "        mGson = new GsonBuilder().serializeNulls().create();\n" +
            "    }\n" +
            "\n" +
            "    public void handle(Socket socket) throws IOException {\n" +
            "        BufferedReader reader = null;\n" +
            "        PrintStream output = null;\n" +
            "        try {\n" +
            "            String route = null;\n" +
            "\n" +
            "            // Read HTTP headers and parse out the route.\n" +
            "            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));\n" +
            "            String line;\n" +
            "            while (!TextUtils.isEmpty(line = reader.readLine())) {\n" +
            "                if (line.startsWith(\"GET /\")) {\n" +
            "                    int start = line.indexOf('/') + 1;\n" +
            "                    int end = line.indexOf(' ', start);\n" +
            "                    route = line.substring(start, end);\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            // Output stream that we send the response to\n" +
            "            output = new PrintStream(socket.getOutputStream());\n" +
            "\n" +
            "            if (route == null || route.isEmpty()) {\n" +
            "                route = \"index.html\";\n" +
            "            }\n" +
            "\n" +
            "            byte[] bytes;\n" +
            "\n" +
            "            if (route.startsWith(\"getDbList\")) {\n" +
            "                final String response = getDBListResponse();\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"getAllDataFromTheTable\")) {\n" +
            "                final String response = getAllDataFromTheTableResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"getTableList\")) {\n" +
            "                final String response = getTableListResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"addTableData\")) {\n" +
            "                final String response = addTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"updateTableData\")) {\n" +
            "                final String response = updateTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"deleteTableData\")) {\n" +
            "                final String response = deleteTableDataAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"query\")) {\n" +
            "                final String response = executeQueryAndGetResponse(route);\n" +
            "                bytes = response.getBytes();\n" +
            "            } else if (route.startsWith(\"downloadDb\")) {\n" +
            "                bytes = Utils.getDatabase(mSelectedDatabase, mDatabaseFiles);\n" +
            "            } else {\n" +
            "                bytes = Utils.loadContent(route, mAssets);\n" +
            "            }\n" +
            "\n" +
            "            if (null == bytes) {\n" +
            "                writeServerError(output);\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            // Send out the content.\n" +
            "            output.println(\"HTTP/1.0 200 OK\");\n" +
            "            output.println(\"Content-Type: \" + Utils.detectMimeType(route));\n" +
            "\n" +
            "            if (route.startsWith(\"downloadDb\")) {\n" +
            "                output.println(\"Content-Disposition: attachment; filename=\" + mSelectedDatabase);\n" +
            "            } else {\n" +
            "                output.println(\"Content-Length: \" + bytes.length);\n" +
            "            }\n" +
            "            output.println();\n" +
            "            output.write(bytes);\n" +
            "            output.flush();\n" +
            "        } finally {\n" +
            "            try {\n" +
            "                if (null != output) {\n" +
            "                    output.close();\n" +
            "                }\n" +
            "                if (null != reader) {\n" +
            "                    reader.close();\n" +
            "                }\n" +
            "            } catch (Exception e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public void setCustomDatabaseFiles(HashMap> customDatabaseFiles) {\n" +
            "        mCustomDatabaseFiles = customDatabaseFiles;\n" +
            "    }\n" +
            "\n" +
            "    private void writeServerError(PrintStream output) {\n" +
            "        output.println(\"HTTP/1.0 500 Internal Server Error\");\n" +
            "        output.flush();\n" +
            "    }\n" +
            "\n" +
            "    private void openDatabase(String database) {\n" +
            "        closeDatabase();\n" +
            "        File databaseFile = mDatabaseFiles.get(database).first;\n" +
            "        String password = mDatabaseFiles.get(database).second;\n" +
            "\n" +
            "        SQLiteDatabase.loadLibs(mContext);\n" +
            "\n" +
            "        mDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFile.getAbsolutePath(), password, null);\n" +
            "        isDbOpened = true;\n" +
            "    }\n" +
            "\n" +
            "    private void closeDatabase() {\n" +
            "        if (mDatabase != null && mDatabase.isOpen()) {\n" +
            "            mDatabase.close();\n" +
            "        }\n" +
            "        mDatabase = null;\n" +
            "        isDbOpened = false;\n" +
            "    }\n" +
            "\n" +
            "    private String getDBListResponse() {\n" +
            "        mDatabaseFiles = DatabaseFileProvider.getDatabaseFiles(mContext);\n" +
            "        if (mCustomDatabaseFiles != null) {\n" +
            "            mDatabaseFiles.putAll(mCustomDatabaseFiles);\n" +
            "        }\n" +
            "        Response response = new Response();\n" +
            "        if (mDatabaseFiles != null) {\n" +
            "            for (HashMap.Entry> entry : mDatabaseFiles.entrySet()) {\n" +
            "                String[] dbEntry = {entry.getKey(), !entry.getValue().second.equals(\"\") ? \"true\" : \"false\"};\n" +
            "                response.rows.add(dbEntry);\n" +
            "            }\n" +
            "        }\n" +
            "        response.rows.add(new String[]{Constants.APP_SHARED_PREFERENCES, \"false\"});\n" +
            "        response.isSuccessful = true;\n" +
            "        return mGson.toJson(response);\n" +
            "    }\n" +
            "\n" +
            "    private String getAllDataFromTheTableResponse(String route) {\n" +
            "\n" +
            "        String tableName = null;\n" +
            "\n" +
            "        if (route.contains(\"?tableName=\")) {\n" +
            "            tableName = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "        }\n" +
            "\n" +
            "        TableDataResponse response;\n" +
            "\n" +
            "        if (isDbOpened) {\n" +
            "            String sql = \"SELECT * FROM \" + tableName;\n" +
            "            response = DatabaseHelper.getTableData(mDatabase, sql, tableName);\n" +
            "        } else {\n" +
            "            response = PrefHelper.getAllPrefData(mContext, tableName);\n" +
            "        }\n" +
            "\n" +
            "        return mGson.toJson(response);\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    private String executeQueryAndGetResponse(String route) {\n" +
            "        String query = null;\n" +
            "        String data = null;\n" +
            "        String first;\n" +
            "        try {\n" +
            "            if (route.contains(\"?query=\")) {\n" +
            "                query = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "            }\n" +
            "            try {\n" +
            "                query = URLDecoder.decode(query, \"UTF-8\");\n" +
            "            } catch (Exception e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "\n" +
            "            if (query != null) {\n" +
            "                String[] statements = query.split(\";\");\n" +
            "\n" +
            "                for (int i = 0; i < statements.length; i++) {\n" +
            "\n" +
            "                    String aQuery = statements[i].trim();\n" +
            "                    first = aQuery.split(\" \")[0].toLowerCase();\n" +
            "                    if (first.equals(\"select\") || first.equals(\"pragma\")) {\n" +
            "                        TableDataResponse response = DatabaseHelper.getTableData(mDatabase, aQuery, null);\n" +
            "                        data = mGson.toJson(response);\n" +
            "                        if (!response.isSuccessful) {\n" +
            "                            break;\n" +
            "                        }\n" +
            "                    } else {\n" +
            "                        TableDataResponse response = DatabaseHelper.exec(mDatabase, aQuery);\n" +
            "                        data = mGson.toJson(response);\n" +
            "                        if (!response.isSuccessful) {\n" +
            "                            break;\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "\n" +
            "        if (data == null) {\n" +
            "            Response response = new Response();\n" +
            "            response.isSuccessful = false;\n" +
            "            data = mGson.toJson(response);\n" +
            "        }\n" +
            "\n" +
            "        return data;\n" +
            "    }\n" +
            "\n" +
            "    private String getTableListResponse(String route) {\n" +
            "        String database = null;\n" +
            "        if (route.contains(\"?database=\")) {\n" +
            "            database = route.substring(route.indexOf(\"=\") + 1, route.length());\n" +
            "        }\n" +
            "\n" +
            "        Response response;\n" +
            "\n" +
            "        if (Constants.APP_SHARED_PREFERENCES.equals(database)) {\n" +
            "            response = PrefHelper.getAllPrefTableName(mContext);\n" +
            "            closeDatabase();\n" +
            "            mSelectedDatabase = Constants.APP_SHARED_PREFERENCES;\n" +
            "        } else {\n" +
            "            openDatabase(database);\n" +
            "            response = DatabaseHelper.getAllTableName(mDatabase);\n" +
            "            mSelectedDatabase = database;\n" +
            "        }\n" +
            "        return mGson.toJson(response);\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private String addTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"addData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.addOrUpdateRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.addRow(mDatabase, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private String updateTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"updatedData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.addOrUpdateRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.updateRow(mDatabase, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private String deleteTableDataAndGetResponse(String route) {\n" +
            "        UpdateRowResponse response;\n" +
            "        try {\n" +
            "            Uri uri = Uri.parse(URLDecoder.decode(route, \"UTF-8\"));\n" +
            "            String tableName = uri.getQueryParameter(\"tableName\");\n" +
            "            String updatedData = uri.getQueryParameter(\"deleteData\");\n" +
            "            List rowDataRequests = mGson.fromJson(updatedData, new TypeToken>() {\n" +
            "            }.getType());\n" +
            "            if (Constants.APP_SHARED_PREFERENCES.equals(mSelectedDatabase)) {\n" +
            "                response = PrefHelper.deleteRow(mContext, tableName, rowDataRequests);\n" +
            "            } else {\n" +
            "                response = DatabaseHelper.deleteRow(mDatabase, tableName, rowDataRequests);\n" +
            "            }\n" +
            "            return mGson.toJson(response);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            response = new UpdateRowResponse();\n" +
            "            response.isSuccessful = false;\n" +
            "            return mGson.toJson(response);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/sqlite/DebugSQLiteDB.java\"\n" +
            "\n" +
            "package com.amitshekhar.sqlite;\n" +
            "\n" +
            "import android.content.ContentValues;\n" +
            "import android.database.Cursor;\n" +
            "import android.database.SQLException;\n" +
            "\n" +
            "import net.sqlcipher.database.SQLiteDatabase;\n" +
            "\n" +
            "/**\n" +
            " * Created by anandgaurav on 12/02/18.\n" +
            " */\n" +
            "\n" +
            "public class DebugSQLiteDB implements SQLiteDB {\n" +
            "\n" +
            "    private final SQLiteDatabase database;\n" +
            "\n" +
            "    public DebugSQLiteDB(SQLiteDatabase database) {\n" +
            "        this.database = database;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int delete(String table, String whereClause, String[] whereArgs) {\n" +
            "        return database.delete(table, whereClause, whereArgs);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public boolean isOpen() {\n" +
            "        return database.isOpen();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void close() {\n" +
            "        database.close();\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Cursor rawQuery(String sql, String[] selectionArgs) {\n" +
            "        return database.rawQuery(sql, selectionArgs);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void execSQL(String sql) throws SQLException {\n" +
            "        database.execSQL(sql);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public long insert(String table, String nullColumnHack, ContentValues values) {\n" +
            "        return database.insert(table, nullColumnHack, values);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {\n" +
            "        return database.update(table, values, whereClause, whereArgs);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int getVersion() {\n" +
            "        return database.getVersion();\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413/debug-db/src/main/java/com/amitshekhar/sqlite/DebugSQLiteDB.java----parent0\"\n" +
            "\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/sqlite/SQLiteDB.java\"\n" +
            "\n" +
            "package com.amitshekhar.sqlite;\n" +
            "\n" +
            "import android.content.ContentValues;\n" +
            "import android.database.Cursor;\n" +
            "import android.database.SQLException;\n" +
            "\n" +
            "\n" +
            "/**\n" +
            " * Created by anandgaurav on 12/02/18.\n" +
            " */\n" +
            "\n" +
            "public interface SQLiteDB {\n" +
            "\n" +
            "    int delete(String table, String whereClause, String[] whereArgs);\n" +
            "\n" +
            "    boolean isOpen();\n" +
            "\n" +
            "    void close();\n" +
            "\n" +
            "    Cursor rawQuery(String sql, String[] selectionArgs);\n" +
            "\n" +
            "    void execSQL(String sql) throws SQLException;\n" +
            "\n" +
            "    long insert(String table, String nullColumnHack, ContentValues values);\n" +
            "\n" +
            "    int update(String table, ContentValues values, String whereClause, String[] whereArgs);\n" +
            "\n" +
            "    int getVersion();\n" +
            "\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413/debug-db/src/main/java/com/amitshekhar/sqlite/SQLiteDB.java----parent0\"\n" +
            "\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/utils/DatabaseHelper.java\"\n" +
            "\n" +
            "/*\n" +
            " *\n" +
            " *  *    Copyright (C) 2016 Amit Shekhar\n" +
            " *  *    Copyright (C) 2011 Android Open Source Project\n" +
            " *  *\n" +
            " *  *    Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " *  *    you may not use this file except in compliance with the License.\n" +
            " *  *    You may obtain a copy of the License at\n" +
            " *  *\n" +
            " *  *        http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *  *\n" +
            " *  *    Unless required by applicable law or agreed to in writing, software\n" +
            " *  *    distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " *  *    See the License for the specific language governing permissions and\n" +
            " *  *    limitations under the License.\n" +
            " *\n" +
            " */\n" +
            "\n" +
            "package com.amitshekhar.utils;\n" +
            "\n" +
            "import android.content.ContentValues;\n" +
            "import android.database.Cursor;\n" +
            "import android.text.TextUtils;\n" +
            "\n" +
            "import com.amitshekhar.model.Response;\n" +
            "import com.amitshekhar.model.RowDataRequest;\n" +
            "import com.amitshekhar.model.TableDataResponse;\n" +
            "import com.amitshekhar.model.UpdateRowResponse;\n" +
            "import com.amitshekhar.sqlite.SQLiteDB;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.HashSet;\n" +
            "import java.util.List;\n" +
            "\n" +
            "/**\n" +
            " * Created by amitshekhar on 06/02/17.\n" +
            " */\n" +
            "\n" +
            "public class DatabaseHelper {\n" +
            "\n" +
            "    private DatabaseHelper() {\n" +
            "        // This class in not publicly instantiable\n" +
            "    }\n" +
            "\n" +
            "    public static Response getAllTableName(SQLiteDB database) {\n" +
            "        Response response = new Response();\n" +
            "        Cursor c = database.rawQuery(\"SELECT name FROM sqlite_master WHERE type='table' OR type='view' ORDER BY name COLLATE NOCASE\", null);\n" +
            "        if (c.moveToFirst()) {\n" +
            "            while (!c.isAfterLast()) {\n" +
            "                response.rows.add(c.getString(0));\n" +
            "                c.moveToNext();\n" +
            "            }\n" +
            "        }\n" +
            "        c.close();\n" +
            "        response.isSuccessful = true;\n" +
            "        try {\n" +
            "            response.dbVersion = database.getVersion();\n" +
            "        } catch (Exception ignore) {\n" +
            "\n" +
            "        }\n" +
            "        return response;\n" +
            "    }\n" +
            "\n" +
            "    public static TableDataResponse getTableData(SQLiteDB db, String selectQuery, String tableName) {\n" +
            "\n" +
            "        TableDataResponse tableData = new TableDataResponse();\n" +
            "        tableData.isSelectQuery = true;\n" +
            "        if (tableName == null) {\n" +
            "            tableName = getTableName(selectQuery);\n" +
            "        }\n" +
            "\n" +
            "        final String quotedTableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        if (tableName != null) {\n" +
            "            final String pragmaQuery = \"PRAGMA table_info(\" + quotedTableName + \")\";\n" +
            "            tableData.tableInfos = getTableInfo(db, pragmaQuery);\n" +
            "        }\n" +
            "        Cursor cursor = null;\n" +
            "        boolean isView = false;\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(\"SELECT type FROM sqlite_master WHERE name=?\",\n" +
            "                    new String[]{quotedTableName});\n" +
            "            if (cursor.moveToFirst()) {\n" +
            "                isView = \"view\".equalsIgnoreCase(cursor.getString(0));\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        } finally {\n" +
            "            if (cursor != null) {\n" +
            "                cursor.close();\n" +
            "            }\n" +
            "        }\n" +
            "        tableData.isEditable = tableName != null && tableData.tableInfos != null && !isView;\n" +
            "\n" +
            "\n" +
            "        if (!TextUtils.isEmpty(tableName)) {\n" +
            "            selectQuery = selectQuery.replace(tableName, quotedTableName);\n" +
            "        }\n" +
            "\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(selectQuery, null);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            tableData.isSuccessful = false;\n" +
            "            tableData.errorMessage = e.getMessage();\n" +
            "            return tableData;\n" +
            "        }\n" +
            "\n" +
            "        if (cursor != null) {\n" +
            "            cursor.moveToFirst();\n" +
            "\n" +
            "            // setting tableInfo when tableName is not known and making\n" +
            "            // it non-editable also by making isPrimary true for all\n" +
            "            if (tableData.tableInfos == null) {\n" +
            "                tableData.tableInfos = new ArrayList<>();\n" +
            "                for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "                    TableDataResponse.TableInfo tableInfo = new TableDataResponse.TableInfo();\n" +
            "                    tableInfo.title = cursor.getColumnName(i);\n" +
            "                    tableInfo.isPrimary = true;\n" +
            "                    tableData.tableInfos.add(tableInfo);\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            tableData.isSuccessful = true;\n" +
            "            tableData.rows = new ArrayList<>();\n" +
            "            if (cursor.getCount() > 0) {\n" +
            "\n" +
            "                do {\n" +
            "                    List row = new ArrayList<>();\n" +
            "                    for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "                        TableDataResponse.ColumnData columnData = new TableDataResponse.ColumnData();\n" +
            "                        switch (cursor.getType(i)) {\n" +
            "                            case Cursor.FIELD_TYPE_BLOB:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = ConverterUtils.blobToString(cursor.getBlob(i));\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_FLOAT:\n" +
            "                                columnData.dataType = DataType.REAL;\n" +
            "                                columnData.value = cursor.getDouble(i);\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_INTEGER:\n" +
            "                                columnData.dataType = DataType.INTEGER;\n" +
            "                                columnData.value = cursor.getLong(i);\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_STRING:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = cursor.getString(i);\n" +
            "                                break;\n" +
            "                            default:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = cursor.getString(i);\n" +
            "                        }\n" +
            "                        row.add(columnData);\n" +
            "                    }\n" +
            "                    tableData.rows.add(row);\n" +
            "\n" +
            "                } while (cursor.moveToNext());\n" +
            "            }\n" +
            "            cursor.close();\n" +
            "            return tableData;\n" +
            "        } else {\n" +
            "            tableData.isSuccessful = false;\n" +
            "            tableData.errorMessage = \"Cursor is null\";\n" +
            "            return tableData;\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private static String getQuotedTableName(String tableName) {\n" +
            "        return String.format(\"[%s]\", tableName);\n" +
            "    }\n" +
            "\n" +
            "    private static List getTableInfo(SQLiteDB db, String pragmaQuery) {\n" +
            "\n" +
            "        Cursor cursor;\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(pragmaQuery, null);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            return null;\n" +
            "        }\n" +
            "\n" +
            "        if (cursor != null) {\n" +
            "\n" +
            "            List tableInfoList = new ArrayList<>();\n" +
            "\n" +
            "            cursor.moveToFirst();\n" +
            "\n" +
            "            if (cursor.getCount() > 0) {\n" +
            "                do {\n" +
            "                    TableDataResponse.TableInfo tableInfo = new TableDataResponse.TableInfo();\n" +
            "\n" +
            "                    for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "\n" +
            "                        final String columnName = cursor.getColumnName(i);\n" +
            "\n" +
            "                        switch (columnName) {\n" +
            "                            case Constants.PK:\n" +
            "                                tableInfo.isPrimary = cursor.getInt(i) == 1;\n" +
            "                                break;\n" +
            "                            case Constants.NAME:\n" +
            "                                tableInfo.title = cursor.getString(i);\n" +
            "                                break;\n" +
            "                            default:\n" +
            "                        }\n" +
            "\n" +
            "                    }\n" +
            "                    tableInfoList.add(tableInfo);\n" +
            "\n" +
            "                } while (cursor.moveToNext());\n" +
            "            }\n" +
            "            cursor.close();\n" +
            "            return tableInfoList;\n" +
            "        }\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse addRow(SQLiteDB db, String tableName,\n" +
            "                                           List rowDataRequests) {\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        ContentValues contentValues = new ContentValues();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "\n" +
            "            switch (rowDataRequest.dataType) {\n" +
            "                case DataType.INTEGER:\n" +
            "                    contentValues.put(rowDataRequest.title, Long.valueOf(rowDataRequest.value));\n" +
            "                    break;\n" +
            "                case DataType.REAL:\n" +
            "                    contentValues.put(rowDataRequest.title, Double.valueOf(rowDataRequest.value));\n" +
            "                    break;\n" +
            "                case DataType.TEXT:\n" +
            "                    contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                    break;\n" +
            "                default:\n" +
            "                    contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                    break;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        long result = db.insert(tableName, null, contentValues);\n" +
            "        updateRowResponse.isSuccessful = result > 0;\n" +
            "\n" +
            "        return updateRowResponse;\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse updateRow(SQLiteDB db, String tableName, List rowDataRequests) {\n" +
            "\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        ContentValues contentValues = new ContentValues();\n" +
            "\n" +
            "        String whereClause = null;\n" +
            "        List whereArgsList = new ArrayList<>();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "            if (rowDataRequest.isPrimary) {\n" +
            "                if (whereClause == null) {\n" +
            "                    whereClause = rowDataRequest.title + \"=? \";\n" +
            "                } else {\n" +
            "                    whereClause = whereClause + \"and \" + rowDataRequest.title + \"=? \";\n" +
            "                }\n" +
            "                whereArgsList.add(rowDataRequest.value);\n" +
            "            } else {\n" +
            "                switch (rowDataRequest.dataType) {\n" +
            "                    case DataType.INTEGER:\n" +
            "                        contentValues.put(rowDataRequest.title, Long.valueOf(rowDataRequest.value));\n" +
            "                        break;\n" +
            "                    case DataType.REAL:\n" +
            "                        contentValues.put(rowDataRequest.title, Double.valueOf(rowDataRequest.value));\n" +
            "                        break;\n" +
            "                    case DataType.TEXT:\n" +
            "                        contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                        break;\n" +
            "                    default:\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        String[] whereArgs = new String[whereArgsList.size()];\n" +
            "\n" +
            "        for (int i = 0; i < whereArgsList.size(); i++) {\n" +
            "            whereArgs[i] = whereArgsList.get(i);\n" +
            "        }\n" +
            "\n" +
            "        db.update(tableName, contentValues, whereClause, whereArgs);\n" +
            "        updateRowResponse.isSuccessful = true;\n" +
            "        return updateRowResponse;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse deleteRow(SQLiteDB db, String tableName,\n" +
            "                                              List rowDataRequests) {\n" +
            "\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "\n" +
            "        String whereClause = null;\n" +
            "        List whereArgsList = new ArrayList<>();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "            if (rowDataRequest.isPrimary) {\n" +
            "                if (whereClause == null) {\n" +
            "                    whereClause = rowDataRequest.title + \"=? \";\n" +
            "                } else {\n" +
            "                    whereClause = whereClause + \"and \" + rowDataRequest.title + \"=? \";\n" +
            "                }\n" +
            "                whereArgsList.add(rowDataRequest.value);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        if (whereArgsList.size() == 0) {\n" +
            "            updateRowResponse.isSuccessful = true;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        String[] whereArgs = new String[whereArgsList.size()];\n" +
            "\n" +
            "        for (int i = 0; i < whereArgsList.size(); i++) {\n" +
            "            whereArgs[i] = whereArgsList.get(i);\n" +
            "        }\n" +
            "\n" +
            "        db.delete(tableName, whereClause, whereArgs);\n" +
            "        updateRowResponse.isSuccessful = true;\n" +
            "        return updateRowResponse;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static TableDataResponse exec(SQLiteDB database, String sql) {\n" +
            "        TableDataResponse tableDataResponse = new TableDataResponse();\n" +
            "        tableDataResponse.isSelectQuery = false;\n" +
            "        try {\n" +
            "\n" +
            "            String tableName = getTableName(sql);\n" +
            "\n" +
            "            if (!TextUtils.isEmpty(tableName)) {\n" +
            "                String quotedTableName = getQuotedTableName(tableName);\n" +
            "                sql = sql.replace(tableName, quotedTableName);\n" +
            "            }\n" +
            "\n" +
            "            database.execSQL(sql);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            tableDataResponse.isSuccessful = false;\n" +
            "            tableDataResponse.errorMessage = e.getMessage();\n" +
            "            return tableDataResponse;\n" +
            "        }\n" +
            "        tableDataResponse.isSuccessful = true;\n" +
            "        return tableDataResponse;\n" +
            "    }\n" +
            "\n" +
            "    private static String getTableName(String selectQuery) {\n" +
            "        // TODO: Handle JOIN Query\n" +
            "        TableNameParser tableNameParser = new TableNameParser(selectQuery);\n" +
            "        HashSet tableNames = (HashSet) tableNameParser.tables();\n" +
            "\n" +
            "        for (String tableName : tableNames) {\n" +
            "            if (!TextUtils.isEmpty(tableName)) {\n" +
            "                return tableName;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413/debug-db/src/main/java/com/amitshekhar/utils/DatabaseHelper.java----parent0\"\n" +
            "\n" +
            "/*\n" +
            " *\n" +
            " *  *    Copyright (C) 2016 Amit Shekhar\n" +
            " *  *    Copyright (C) 2011 Android Open Source Project\n" +
            " *  *\n" +
            " *  *    Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " *  *    you may not use this file except in compliance with the License.\n" +
            " *  *    You may obtain a copy of the License at\n" +
            " *  *\n" +
            " *  *        http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *  *\n" +
            " *  *    Unless required by applicable law or agreed to in writing, software\n" +
            " *  *    distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " *  *    See the License for the specific language governing permissions and\n" +
            " *  *    limitations under the License.\n" +
            " *\n" +
            " */\n" +
            "\n" +
            "package com.amitshekhar.utils;\n" +
            "\n" +
            "import android.content.ContentValues;\n" +
            "import android.text.TextUtils;\n" +
            "\n" +
            "import com.amitshekhar.model.Response;\n" +
            "import com.amitshekhar.model.RowDataRequest;\n" +
            "import com.amitshekhar.model.TableDataResponse;\n" +
            "import com.amitshekhar.model.UpdateRowResponse;\n" +
            "\n" +
            "import net.sqlcipher.Cursor;\n" +
            "import net.sqlcipher.database.SQLiteDatabase;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.HashSet;\n" +
            "import java.util.List;\n" +
            "\n" +
            "/**\n" +
            " * Created by amitshekhar on 06/02/17.\n" +
            " */\n" +
            "\n" +
            "public class DatabaseHelper {\n" +
            "\n" +
            "    private DatabaseHelper() {\n" +
            "        // This class in not publicly instantiable\n" +
            "    }\n" +
            "\n" +
            "    public static Response getAllTableName(SQLiteDatabase database) {\n" +
            "        Response response = new Response();\n" +
            "        Cursor c = database.rawQuery(\"SELECT name FROM sqlite_master WHERE type='table' OR type='view' ORDER BY name COLLATE NOCASE\", null);\n" +
            "        if (c.moveToFirst()) {\n" +
            "            while (!c.isAfterLast()) {\n" +
            "                response.rows.add(c.getString(0));\n" +
            "                c.moveToNext();\n" +
            "            }\n" +
            "        }\n" +
            "        c.close();\n" +
            "        response.isSuccessful = true;\n" +
            "        try {\n" +
            "            response.dbVersion = database.getVersion();\n" +
            "        } catch (Exception ignore) {\n" +
            "\n" +
            "        }\n" +
            "        return response;\n" +
            "    }\n" +
            "\n" +
            "    public static TableDataResponse getTableData(SQLiteDatabase db, String selectQuery, String tableName) {\n" +
            "\n" +
            "        TableDataResponse tableData = new TableDataResponse();\n" +
            "        tableData.isSelectQuery = true;\n" +
            "        if (tableName == null) {\n" +
            "            tableName = getTableName(selectQuery);\n" +
            "        }\n" +
            "\n" +
            "        final String quotedTableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        if (tableName != null) {\n" +
            "            final String pragmaQuery = \"PRAGMA table_info(\" + quotedTableName + \")\";\n" +
            "            tableData.tableInfos = getTableInfo(db, pragmaQuery);\n" +
            "        }\n" +
            "        Cursor cursor = null;\n" +
            "        boolean isView = false;\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(\"SELECT type FROM sqlite_master WHERE name=?\",\n" +
            "                    new String[]{quotedTableName});\n" +
            "            if (cursor.moveToFirst()) {\n" +
            "                isView = \"view\".equalsIgnoreCase(cursor.getString(0));\n" +
            "            }\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        } finally {\n" +
            "            if (cursor != null) {\n" +
            "                cursor.close();\n" +
            "            }\n" +
            "        }\n" +
            "        tableData.isEditable = tableName != null && tableData.tableInfos != null && !isView;\n" +
            "\n" +
            "\n" +
            "        if (!TextUtils.isEmpty(tableName)) {\n" +
            "            selectQuery = selectQuery.replace(tableName, quotedTableName);\n" +
            "        }\n" +
            "\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(selectQuery, null);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            tableData.isSuccessful = false;\n" +
            "            tableData.errorMessage = e.getMessage();\n" +
            "            return tableData;\n" +
            "        }\n" +
            "\n" +
            "        if (cursor != null) {\n" +
            "            cursor.moveToFirst();\n" +
            "\n" +
            "            // setting tableInfo when tableName is not known and making\n" +
            "            // it non-editable also by making isPrimary true for all\n" +
            "            if (tableData.tableInfos == null) {\n" +
            "                tableData.tableInfos = new ArrayList<>();\n" +
            "                for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "                    TableDataResponse.TableInfo tableInfo = new TableDataResponse.TableInfo();\n" +
            "                    tableInfo.title = cursor.getColumnName(i);\n" +
            "                    tableInfo.isPrimary = true;\n" +
            "                    tableData.tableInfos.add(tableInfo);\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            tableData.isSuccessful = true;\n" +
            "            tableData.rows = new ArrayList<>();\n" +
            "            if (cursor.getCount() > 0) {\n" +
            "\n" +
            "                do {\n" +
            "                    List row = new ArrayList<>();\n" +
            "                    for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "                        TableDataResponse.ColumnData columnData = new TableDataResponse.ColumnData();\n" +
            "                        switch (cursor.getType(i)) {\n" +
            "                            case Cursor.FIELD_TYPE_BLOB:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = ConverterUtils.blobToString(cursor.getBlob(i));\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_FLOAT:\n" +
            "                                columnData.dataType = DataType.REAL;\n" +
            "                                columnData.value = cursor.getDouble(i);\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_INTEGER:\n" +
            "                                columnData.dataType = DataType.INTEGER;\n" +
            "                                columnData.value = cursor.getLong(i);\n" +
            "                                break;\n" +
            "                            case Cursor.FIELD_TYPE_STRING:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = cursor.getString(i);\n" +
            "                                break;\n" +
            "                            default:\n" +
            "                                columnData.dataType = DataType.TEXT;\n" +
            "                                columnData.value = cursor.getString(i);\n" +
            "                        }\n" +
            "                        row.add(columnData);\n" +
            "                    }\n" +
            "                    tableData.rows.add(row);\n" +
            "\n" +
            "                } while (cursor.moveToNext());\n" +
            "            }\n" +
            "            cursor.close();\n" +
            "            return tableData;\n" +
            "        } else {\n" +
            "            tableData.isSuccessful = false;\n" +
            "            tableData.errorMessage = \"Cursor is null\";\n" +
            "            return tableData;\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private static String getQuotedTableName(String tableName) {\n" +
            "        return String.format(\"[%s]\", tableName);\n" +
            "    }\n" +
            "\n" +
            "    private static List getTableInfo(SQLiteDatabase db, String pragmaQuery) {\n" +
            "\n" +
            "        Cursor cursor;\n" +
            "        try {\n" +
            "            cursor = db.rawQuery(pragmaQuery, null);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            return null;\n" +
            "        }\n" +
            "\n" +
            "        if (cursor != null) {\n" +
            "\n" +
            "            List tableInfoList = new ArrayList<>();\n" +
            "\n" +
            "            cursor.moveToFirst();\n" +
            "\n" +
            "            if (cursor.getCount() > 0) {\n" +
            "                do {\n" +
            "                    TableDataResponse.TableInfo tableInfo = new TableDataResponse.TableInfo();\n" +
            "\n" +
            "                    for (int i = 0; i < cursor.getColumnCount(); i++) {\n" +
            "\n" +
            "                        final String columnName = cursor.getColumnName(i);\n" +
            "\n" +
            "                        switch (columnName) {\n" +
            "                            case Constants.PK:\n" +
            "                                tableInfo.isPrimary = cursor.getInt(i) == 1;\n" +
            "                                break;\n" +
            "                            case Constants.NAME:\n" +
            "                                tableInfo.title = cursor.getString(i);\n" +
            "                                break;\n" +
            "                            default:\n" +
            "                        }\n" +
            "\n" +
            "                    }\n" +
            "                    tableInfoList.add(tableInfo);\n" +
            "\n" +
            "                } while (cursor.moveToNext());\n" +
            "            }\n" +
            "            cursor.close();\n" +
            "            return tableInfoList;\n" +
            "        }\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse addRow(SQLiteDatabase db, String tableName,\n" +
            "                                           List rowDataRequests) {\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        ContentValues contentValues = new ContentValues();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "\n" +
            "            switch (rowDataRequest.dataType) {\n" +
            "                case DataType.INTEGER:\n" +
            "                    contentValues.put(rowDataRequest.title, Long.valueOf(rowDataRequest.value));\n" +
            "                    break;\n" +
            "                case DataType.REAL:\n" +
            "                    contentValues.put(rowDataRequest.title, Double.valueOf(rowDataRequest.value));\n" +
            "                    break;\n" +
            "                case DataType.TEXT:\n" +
            "                    contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                    break;\n" +
            "                default:\n" +
            "                    contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                    break;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        long result = db.insert(tableName, null, contentValues);\n" +
            "        updateRowResponse.isSuccessful = result > 0;\n" +
            "\n" +
            "        return updateRowResponse;\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse updateRow(SQLiteDatabase db, String tableName, List rowDataRequests) {\n" +
            "\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "        ContentValues contentValues = new ContentValues();\n" +
            "\n" +
            "        String whereClause = null;\n" +
            "        List whereArgsList = new ArrayList<>();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "            if (rowDataRequest.isPrimary) {\n" +
            "                if (whereClause == null) {\n" +
            "                    whereClause = rowDataRequest.title + \"=? \";\n" +
            "                } else {\n" +
            "                    whereClause = whereClause + \"and \" + rowDataRequest.title + \"=? \";\n" +
            "                }\n" +
            "                whereArgsList.add(rowDataRequest.value);\n" +
            "            } else {\n" +
            "                switch (rowDataRequest.dataType) {\n" +
            "                    case DataType.INTEGER:\n" +
            "                        contentValues.put(rowDataRequest.title, Long.valueOf(rowDataRequest.value));\n" +
            "                        break;\n" +
            "                    case DataType.REAL:\n" +
            "                        contentValues.put(rowDataRequest.title, Double.valueOf(rowDataRequest.value));\n" +
            "                        break;\n" +
            "                    case DataType.TEXT:\n" +
            "                        contentValues.put(rowDataRequest.title, rowDataRequest.value);\n" +
            "                        break;\n" +
            "                    default:\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        String[] whereArgs = new String[whereArgsList.size()];\n" +
            "\n" +
            "        for (int i = 0; i < whereArgsList.size(); i++) {\n" +
            "            whereArgs[i] = whereArgsList.get(i);\n" +
            "        }\n" +
            "\n" +
            "        db.update(tableName, contentValues, whereClause, whereArgs);\n" +
            "        updateRowResponse.isSuccessful = true;\n" +
            "        return updateRowResponse;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static UpdateRowResponse deleteRow(SQLiteDatabase db, String tableName,\n" +
            "                                              List rowDataRequests) {\n" +
            "\n" +
            "        UpdateRowResponse updateRowResponse = new UpdateRowResponse();\n" +
            "\n" +
            "        if (rowDataRequests == null || tableName == null) {\n" +
            "            updateRowResponse.isSuccessful = false;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        tableName = getQuotedTableName(tableName);\n" +
            "\n" +
            "\n" +
            "        String whereClause = null;\n" +
            "        List whereArgsList = new ArrayList<>();\n" +
            "\n" +
            "        for (RowDataRequest rowDataRequest : rowDataRequests) {\n" +
            "            if (Constants.NULL.equals(rowDataRequest.value)) {\n" +
            "                rowDataRequest.value = null;\n" +
            "            }\n" +
            "            if (rowDataRequest.isPrimary) {\n" +
            "                if (whereClause == null) {\n" +
            "                    whereClause = rowDataRequest.title + \"=? \";\n" +
            "                } else {\n" +
            "                    whereClause = whereClause + \"and \" + rowDataRequest.title + \"=? \";\n" +
            "                }\n" +
            "                whereArgsList.add(rowDataRequest.value);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        if (whereArgsList.size() == 0) {\n" +
            "            updateRowResponse.isSuccessful = true;\n" +
            "            return updateRowResponse;\n" +
            "        }\n" +
            "\n" +
            "        String[] whereArgs = new String[whereArgsList.size()];\n" +
            "\n" +
            "        for (int i = 0; i < whereArgsList.size(); i++) {\n" +
            "            whereArgs[i] = whereArgsList.get(i);\n" +
            "        }\n" +
            "\n" +
            "        db.delete(tableName, whereClause, whereArgs);\n" +
            "        updateRowResponse.isSuccessful = true;\n" +
            "        return updateRowResponse;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    public static TableDataResponse exec(SQLiteDatabase database, String sql) {\n" +
            "        TableDataResponse tableDataResponse = new TableDataResponse();\n" +
            "        tableDataResponse.isSelectQuery = false;\n" +
            "        try {\n" +
            "\n" +
            "            String tableName = getTableName(sql);\n" +
            "\n" +
            "            if (!TextUtils.isEmpty(tableName)) {\n" +
            "                String quotedTableName = getQuotedTableName(tableName);\n" +
            "                sql = sql.replace(tableName, quotedTableName);\n" +
            "            }\n" +
            "\n" +
            "            database.execSQL(sql);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            tableDataResponse.isSuccessful = false;\n" +
            "            tableDataResponse.errorMessage = e.getMessage();\n" +
            "            return tableDataResponse;\n" +
            "        }\n" +
            "        tableDataResponse.isSuccessful = true;\n" +
            "        return tableDataResponse;\n" +
            "    }\n" +
            "\n" +
            "    private static String getTableName(String selectQuery) {\n" +
            "        // TODO: 24/4/17 Handle JOIN Query\n" +
            "        TableNameParser tableNameParser = new TableNameParser(selectQuery);\n" +
            "        HashSet tableNames = (HashSet) tableNameParser.tables();\n" +
            "\n" +
            "        for (String tableName : tableNames) {\n" +
            "            if (!TextUtils.isEmpty(tableName)) {\n" +
            "                return tableName;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "--xxx---------------xxx\n" +
            "Content-Disposition: form-data; name=\"meta\"\n" +
            "\n" +
            "{\"author\": \"amitshekhariitbhu\", \"date_time\": \"2018-02-12T13:16:11Z\", \"committer\": \"anandgaurav10\", \"commit_hash\": \"43e48d15e6ee435ed0b1abc6d76638dc8bf0217d\", \"commit_log\": \"\\n      Provide database instance through interface\\n    \", \"children\": null, \"parents\": [\"52d1cbab7903ff3dda0dbdb8eb7e21f6ed6f0413\"]}\n" +
            "--xxx---------------xxx--\n" +
            "\n";

    public static void main(String[] args) {
        final String DIVIDER = "--xxx---------------xxx";
        String[] data = response.split(DIVIDER);
        if (data.length <= 1) {
            return;
        }
        int size = data.length;
        //找到meta信息
        Meta meta = filterMeta(data[size - 2]);
        //建立一个文件夹
        File folder = createDirectory("data/" + meta.getCommit_hash());
        createFile("meta", new Gson().toJson(meta), folder);
        for (String content : data) {
            String info = content.split("\n\n")[0];//标题
            String codeContent = content.substring(info.length()); //正文
            // "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435
            // ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/server/RequestHandler.java\"
            //匹配name字段
            String regex = "https://github.com[a-zA-z0-9\\_\\-/.]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(info);
            if (matcher.find()) {
                String name = matcher.group();
                createFile(name, codeContent, folder);
            }
        }
    }

    /**
     * 解析meta信息
     *
     * @param datum
     * @return
     */
    private static Meta filterMeta(String datum) {
        //{"autho
        String[] metaInfo = datum.split("\n\n");
        //匹配
        for (String meta : metaInfo) {
            if (meta.startsWith("{\"")) {
                //找到当前meta信息
                Meta metaObj = new Gson().fromJson(meta, Meta.class);
                return metaObj;
            }
        }
        return null;
    }


    /**
     * 新建文件
     *
     * @param s
     * @param content
     * @param folder
     */
    private static void createFile(String fileName, String codeContent, File folder) {
        FileUtil.createFile(fileName, codeContent, folder);

    }

    /**
     * 创建一个文件夹
     *
     * @param directory
     */
    private static File createDirectory(String directory) {
        return FileUtil.createFolder(directory);
    }

}
