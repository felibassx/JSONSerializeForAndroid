package com.example.kafec.todolistexample;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {

    private String mFilename; // nombre del fichero JSON
    private Context mContext; // contexto donde se guardara el fichero

    //Constructor del objeto que serializará en ficheros
    public JSONSerializer(String filename, Context context){
        this.mFilename = filename;
        this.mContext = context;
    }

    public void save(List<Note> notes) throws IOException, JSONException{

        //array de objetos JSON
        JSONArray jArray = new JSONArray();

        //Convertir cada objeto de la lista en JSONObject y guardarlos en el JSON Array
        for(Note n : notes){
            jArray.put(n.convertNoteToJSON());
        }

        //Para guardar el fichero JSON, hay que usar un Writer
        Writer writer = null;

        try {

            //Output Stream abre el fichero donde guardaremos el JSON
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);

            //el escritor sabe donde escribir su contenido
            writer = new OutputStreamWriter(out);

            //escribe en el disco el array
            writer.write(jArray.toString());
        }
        finally {
            if(null != writer){
                writer.close();
            }
        }

    }

    public ArrayList<Note> load() throws IOException, JSONException{
        ArrayList<Note> notes = new ArrayList<Note>();

        //Leer el fichero
        BufferedReader reader = null;

        try{
            InputStream in = mContext.openFileInput(mFilename);

            reader = new BufferedReader(new InputStreamReader(in));

            //Leer los string del fichero json
            StringBuilder jsonString = new StringBuilder();

            String currentLine = null;

            //Leer fichero entero y pasarlo a string
            while((currentLine = reader.readLine())!=null){
                jsonString.append(currentLine);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                notes.add(new Note(jArray.getJSONObject(i)));
            }


        }catch (FileNotFoundException e){

        }finally {
            if(reader != null)
                reader.close();
        }

        return notes;


    }
}
