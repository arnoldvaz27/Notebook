package com.example.notebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity{

    private ImageView menuOne,menuTwo,menuThree,menuFour,menuFive;
    private EditText fullNote;
    private TextView See;

    private String value;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializedField();

        menuOne.setVisibility(View.GONE);
        menuThree.setVisibility(View.GONE);
        menuFour.setVisibility(View.GONE);
        menuFive.setVisibility(View.GONE);
        ButtonClicks();

    }

    private void ButtonClicks() {
        menuOne.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, menuOne);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                try {
                    SaveFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            });


            popup.show();//showing popup menu for saving as pdf
        });
        menuTwo.setOnClickListener(this::Click);

        menuThree.setOnClickListener(v -> {
            menuFive.setVisibility(View.VISIBLE);
            menuThree.setVisibility(View.GONE);
            menuFour.setVisibility(View.VISIBLE);
            menuOne.setVisibility(View.VISIBLE);
            fullNote.setVisibility(View.VISIBLE);
            SharedPreferences getShared = getSharedPreferences("My_Note",MODE_PRIVATE);
            value = getShared.getString(AppData.MyNote,null);
            if(value!=null)
            {
                fullNote.setText(value);
                fullNote.setSelection(fullNote.getText().length());
            }


            Toast.makeText(MainActivity.this, "Book Opened", Toast.LENGTH_SHORT).show();
        });

        menuFive.setOnClickListener(v -> {
            menuThree.setVisibility(View.GONE);
            if(TextUtils.isEmpty(fullNote.getText().toString()))
            {
                Toast.makeText(MainActivity.this, "You Need Write Something in order to save", Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences shard = getSharedPreferences("My_Note",MODE_PRIVATE);
                final SharedPreferences.Editor editor = shard.edit();
                editor.putString(AppData.MyNote,fullNote.getText().toString());
                editor.apply();
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        menuFour.setOnClickListener(v -> {
            menuFive.setVisibility(View.GONE);
            menuFour.setVisibility(View.GONE);
            menuTwo.setVisibility(View.VISIBLE);
            fullNote.setVisibility(View.GONE);
            menuOne.setVisibility(View.GONE);
            fullNote.setVisibility(View.GONE);
            SharedPreferences shard = getSharedPreferences("My_Note",MODE_PRIVATE);
            final SharedPreferences.Editor editor = shard.edit();
            AppData.MyNote = "";
            editor.putString(AppData.MyNote,null);
            editor.apply();
            fullNote.setText("");
            See.setText("");
            Toast.makeText(MainActivity.this, "Book Closed", Toast.LENGTH_SHORT).show();

        });

    }

    private void SaveFile() throws Exception{

        if(AppData.MyNote==null)
        {
            Toast.makeText(MainActivity.this, "You have to give file name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String fileName = AppData.MyNote;
            fileName = fileName.replaceAll("/","-");
            if(TextUtils.isEmpty(fullNote.getText().toString()))
            {
                Toast.makeText(MainActivity.this, "File Could not be stored as it is empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String text = fullNote.getText().toString();

                int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                            showMessage((dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS));
                            return;
                        }
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                    }
                } else {
                    Toast.makeText(this, "PDF copy has been saved to this device successfully", Toast.LENGTH_LONG).show();
                    File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Notebook/");
                    if (!docsFolder.exists()) {
                        docsFolder.mkdir();
                    }
                    File pdfFile = new File(docsFolder.getAbsolutePath(), fileName + ".pdf");
                    OutputStream outputStream = new FileOutputStream(pdfFile);
                    Document document = new Document();
                    PdfWriter.getInstance(document, outputStream);
                    document.open();

                    Font f = new Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.NORMAL, BaseColor.BLACK);
                    document.add(new Paragraph(text, f));


                    document.close();
                }

            }
        }
    }

    //dialog shown when the user has not given permission to write to the external storage
    private void showMessage(DialogInterface.OnClickListener onClickListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage("You need to allow to Storage")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", onClickListener)
                .create()
                .show();
    }

    private void InitializedField() {

        menuOne = findViewById(R.id.image1);
        menuTwo = findViewById(R.id.image2);
        menuThree = findViewById(R.id.image3);
        menuFour = findViewById(R.id.image4);
        menuFive = findViewById(R.id.image5);
        fullNote = findViewById(R.id.noteText);
        See = findViewById(R.id.noteSee);
    }

    public void Click(View v) {

        if (v == menuTwo) {

            // Get Current Chosen Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {

                        AppData.MyNote =(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        See.setText(AppData.MyNote);
                        menuTwo.setVisibility(View.GONE);

                        menuThree.setVisibility(View.VISIBLE);
                        int yearChange  = year - 2000;
                        int monthChange = monthOfYear+1;
                        String yearLocally = Integer.toString(yearChange);
                        String monthLocally = Integer.toString(monthChange);
                        String dayLocally = Integer.toString(dayOfMonth);
                        AppData.Date = (yearLocally+monthLocally+dayLocally);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.info,menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.process) {
            CheckRightOrWrong();
        }
        if (item.getItemId() == R.id.info) {
            Info();
        }
        return true;
    }

    private void Info() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Important Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(MainActivity.this);
        groupNameField.setText("1) You need to follow the steps in order to use your notebook efficiently, Steps are provided in the process icon just next to the info icon. \n\n2) You cannot open other date's notebook before closing the current notebook. \n\n3) If You wish to not save your content then you can directly close your book. \n\n4) If there is no content in the date's notebook it will tell u to write your note");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    private void CheckRightOrWrong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Steps To Follow to Save Your Work");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(MainActivity.this);
        groupNameField.setText("1) Select the date from the calendar \n\n" +
                "2) Open your notebook by clicking on open book icon \n\n" +
                "3) Write Your Note where the cursor is blinking \n\n" +
                "4) After writing you need to click on the right icon in order to save your note \n\n" +
                "5) After Saving Your note you can close your notebook by clicking on the close book icon");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();

    }

}