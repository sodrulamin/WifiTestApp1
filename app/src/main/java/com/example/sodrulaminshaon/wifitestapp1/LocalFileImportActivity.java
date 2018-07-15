package com.example.sodrulaminshaon.wifitestapp1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author android and Ifta
 */
public class LocalFileImportActivity extends AppCompatActivity {
    public class SelectionMode {
        public static final int MODE_CREATE = 0;

        public static final int MODE_OPEN = 1;
    }
    private static final String ITEM_KEY = "key";
    private static final String ITEM_IMAGE = "image";
    private static String ROOT = "/";
    public static final String START_PATH = "START_PATH";
    public static final String FORMAT_FILTER = "FORMAT_FILTER";
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String SELECTION_MODE = "SELECTION_MODE";
    public static final String CAN_SELECT_DIR = "CAN_SELECT_DIR";
    private List<String> path = null;
    private TextView myPath;
    private EditText mFileName;
    private ArrayList<HashMap<String, Object>> mList;
    private Button selectButton;
    private LinearLayout layoutSelect;
    private LinearLayout layoutCreate;
    private InputMethodManager inputManager;
    private String parentPath;
    private String currentPath = ROOT;
    private int selectionMode = SelectionMode.MODE_CREATE;
    private String[] formatFilter = null;
    private boolean canSelectDir = false;
    private File selectedFile;
    private HashMap<String, Integer> lastPositions = new HashMap<String, Integer>();
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED, getIntent());

        setContentView(R.layout.file_dialog_main);
        lv = (ListView) findViewById(R.id.lv);
        myPath = (TextView) findViewById(R.id.path);
        mFileName = (EditText) findViewById(R.id.fdEditTextFile);
        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        selectButton = (Button) findViewById(R.id.fdButtonSelect);
        selectButton.setEnabled(false);
        selectButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedFile != null) {
                    getIntent().putExtra(RESULT_PATH, selectedFile.getPath());
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });

        final Button newButton = (Button) findViewById(R.id.fdButtonNew);
        newButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setCreateVisible(v);

                mFileName.setText("");
                mFileName.requestFocus();
            }
        });

        selectionMode = getIntent().getIntExtra(SELECTION_MODE, SelectionMode.MODE_CREATE);

        formatFilter = getIntent().getStringArrayExtra(FORMAT_FILTER);

        canSelectDir = getIntent().getBooleanExtra(CAN_SELECT_DIR, false);

        if (selectionMode == SelectionMode.MODE_OPEN) {
            newButton.setEnabled(false);
        }

        layoutSelect = (LinearLayout) findViewById(R.id.fdLinearLayoutSelect);
        layoutCreate = (LinearLayout) findViewById(R.id.fdLinearLayoutCreate);
        layoutCreate.setVisibility(View.GONE);

        final Button cancelButton = (Button) findViewById(R.id.fdButtonCancel);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setSelectVisible(v);
            }

        });
        final Button createButton = (Button) findViewById(R.id.fdButtonCreate);
        createButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFileName.getText().length() > 0) {
                    getIntent().putExtra(RESULT_PATH, currentPath + "/" + mFileName.getText());
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });

        String startPath = getIntent().getStringExtra(START_PATH);
        Log.d("Abhi", "Start Path: " + startPath);
        startPath = startPath != null ? startPath : ROOT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ROOT = startPath;
        }

        if (canSelectDir) {
            File file = new File(startPath);
            selectedFile = file;
            selectButton.setEnabled(true);
        }
        getDirectory(startPath);
    }




    private void getDirectory(String dirPath) {

        boolean useAutoSelection = dirPath.length() < currentPath.length();

        Integer position = lastPositions.get(parentPath);

        getDirImpl(dirPath);

        if (position != null && useAutoSelection) {
            lv.setSelection(position);
        }

    }

    private void getDirImpl(final String dirPath) {

        currentPath = dirPath;

        final List<String> item = new ArrayList<String>();
        path = new ArrayList<String>();
        mList = new ArrayList<HashMap<String, Object>>();

        File f = new File(currentPath);
        File[] files = f.listFiles();
        if (files == null) {
            currentPath = ROOT;
            f = new File(currentPath);
            files = f.listFiles();
        }
        myPath.setText("Location " + ": " + currentPath);

        if (!currentPath.equals(ROOT)) {

            item.add(ROOT);
            addItem(ROOT, R.drawable.ic_folder_open_holo);
            path.add(ROOT);

            item.add("../");
            addItem("../", R.drawable.ic_folder_parent_holo);
            path.add(f.getParent());
            parentPath = f.getParent();

        }

        TreeMap<String, String> dirsMap = new TreeMap<String, String>();
        TreeMap<String, String> dirsPathMap = new TreeMap<String, String>();
        TreeMap<String, String> filesMap = new TreeMap<String, String>();
        TreeMap<String, String> filesPathMap = new TreeMap<String, String>();
        for (File file : files) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                dirsMap.put(dirName, dirName);
                dirsPathMap.put(dirName, file.getPath());
            } else {
                final String fileName = file.getName();
                final String fileNameLwr = fileName.toLowerCase();
                // se ha um filtro de formatos, utiliza-o
                if (formatFilter != null) {
                    boolean contains = false;
                    for (int i = 0; i < formatFilter.length; i++) {
                        final String formatLwr = formatFilter[i].toLowerCase();
                        if (fileNameLwr.endsWith(formatLwr)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        filesMap.put(fileName, fileName);
                        filesPathMap.put(fileName, file.getPath());
                    }
                    // senao, adiciona todos os arquivos
                } else {
                    filesMap.put(fileName, fileName);
                    filesPathMap.put(fileName, file.getPath());
                }
            }
        }
        item.addAll(dirsMap.tailMap("").values());
        item.addAll(filesMap.tailMap("").values());
        path.addAll(dirsPathMap.tailMap("").values());
        path.addAll(filesPathMap.tailMap("").values());

        SimpleAdapter fileList = new SimpleAdapter(this, mList, R.layout.file_dialog_row, new String[]{
                ITEM_KEY, ITEM_IMAGE}, new int[]{R.id.fdrowtext, R.id.fdrowimage});

        for (String dir : dirsMap.tailMap("").values()) {
            addItem(dir, R.drawable.ic_folder_open_holo);
        }

        for (String file : filesMap.tailMap("").values()) {
            addItem(file, R.drawable.ic_folder_open_holo);
        }

        fileList.notifyDataSetChanged();

        lv.setAdapter(fileList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(path.get(position));

                setSelectVisible(view);

                if (file.isDirectory()) {
                    selectButton.setEnabled(false);
                    if (file.canRead()) {
                        lastPositions.put(currentPath, position);
                        getDirectory(path.get(position));
                        if (canSelectDir) {
                            selectedFile = file;
                            view.setSelected(true);
                            selectButton.setEnabled(true);
                        }
                    } else {
                        new AlertDialog.Builder(LocalFileImportActivity.this).setIcon(R.mipmap.ic_launcher)
                                .setTitle("[" + file.getName() + "] ")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                } else {
                    selectedFile = file;
//			v.setSelected(true);
//            v.setBackgroundResource(R.color.blue);
//			selectButton.setEnabled(true);
                    getIntent().putExtra(RESULT_PATH, selectedFile.getPath());
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            selectButton.setEnabled(false);
            if (layoutCreate.getVisibility() == View.VISIBLE) {
                layoutCreate.setVisibility(View.GONE);
                layoutSelect.setVisibility(View.VISIBLE);
            } else {
                if (!currentPath.equals(ROOT)) {
                    getDirectory(parentPath);
                } else {
                    finish();
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(ITEM_KEY, fileName);
        item.put(ITEM_IMAGE, imageId);
        mList.add(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            selectButton.setEnabled(false);

            if (layoutCreate.getVisibility() == View.VISIBLE) {
                layoutCreate.setVisibility(View.GONE);
                layoutSelect.setVisibility(View.VISIBLE);
            } else {
                if (!currentPath.equals(ROOT)) {
                    getDirectory(parentPath);
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Define se o botao de CREATE e visivel.
     *
     * @param v
     */
    private void setCreateVisible(View v) {
        layoutCreate.setVisibility(View.VISIBLE);
        layoutSelect.setVisibility(View.GONE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        selectButton.setEnabled(false);
    }

    /**
     * Define se o botao de SELECT e visivel.
     *
     * @param v
     */
    private void setSelectVisible(View v) {
        layoutCreate.setVisibility(View.GONE);
        layoutSelect.setVisibility(View.VISIBLE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        selectButton.setEnabled(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String filePath = getLocalDriveFilePath(data);
            System.out.println("\n\n***************************** File path: "+filePath+"******************************************\n\n");
        }
    }

    private String getLocalDriveFilePath(Intent intent) {
        if (intent == null || !intent.hasExtra("RESULT_PATH")) {
            return "";
        }
//        Toast.makeText(getActivity(), intent.getStringExtra("RESULT_PATH"), Toast.LENGTH_LONG).show();
        return intent.getStringExtra("RESULT_PATH");
    }
}
