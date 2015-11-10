package com.exsoft.cika.dataguru;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.exsoft.cika.dataguru.domain.Guru;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    Guru guru = new Guru();
    DBAdapter dbAdapter = null;

    EditText txtNama, txtAlamat;
    ListView listGuru;
    Button btnSimpan;
    Guru editGuru;

    private static final String OPTION[] = {"Edit", "Delete"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getApplicationContext());

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtAlamat = (EditText) findViewById(R.id.txtAlamat);
        listGuru = (ListView) findViewById(R.id.listGuru);

        listGuru.setOnItemClickListener(new ListItemClick());
        listGuru.setAdapter(new ListGuruAdapter()(dbAdapter
                .getAllGuru()));
    }

    public class ListItemClick implements AdapterView
            .OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
                                int position, long id) {
            // TODO Auto-generated method stub
            final Guru guru = (Guru) listGuru
                    .getItemAtPosition(position);
            showOptionDialog(guru);
        }
    }

    public void showOptionDialog(Guru guru) {
        final Guru mGuru;
        mGuru = guru;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Test")
                .setItems(OPTION, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int post) {
                        // TODO Auto-generated method stub
                        switch (post) {
                            case 0:
                                editGuru = mGuru;
                                txtNama.setText(mGuru.getNama());
                                txtAlamat.setText(mGuru.getAlamat());
                                btnSimpan.setText("Edit");
                                break;
                            case 1:
                                dbAdapter.delete(mGuru);
                                listGuru.setAdapter
                                        (new ListGuruAdapter(dbAdapter.getAllGuru()));
                                break;
                            default:
                                break;
                        }
                    }
                });
        final Dialog d = builder.create();
        d.show();
    }

    public void save(View v) {
        if(txtNama.getText().length() == 0 ||
                txtAlamat.getText().length() == 0) {
            txtNama.setError("Cannot Empty");
            txtAlamat.setError("Cannot Empty");
        } else {
            if(btnSimpan.getText().equals("Edit")) {
                editGuru.setNama(txtNama.getText().toString());
                editGuru.setAlamat(txtAlamat.getText().toString());
                dbAdapter.updateGuru(editGuru);
                btnSimpan.setText("Simpan");
            } else {
                guru.setNama(txtNama.getText().toString());
                guru.setAlamat(txtAlamat.getText().toString());
                dbAdapter.save(guru);
            }
            txtNama.setText("");
            txtAlamat.setText("");
        }
        listGuru.setAdapter(new ListGuruAdapter(dbAdapter
                .getAllGuru()));
    }

    public class ListGuruAdapter extends BaseAdapter {
        private List<Guru> listGuru;

        public ListGuruAdapter (List<Guru> listGuru) {
            this.listGuru = listGuru;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return this.listGuru.size();
        }

        @Override
        public Guru getItem(int position) {
            // TODO Auto-generated method stub
            return this.listGuru.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView == null) {
                convertView = LayoutInflater
                        .from(getApplicationContext())
                        .inflate(R.layout.list_layout, parent, false);
            }
            final Guru guru = getItem(position);
            if(guru != null) {
                TextView labelNama = (TextView) convertView
                        .findViewById(R.id.labelNama);
                labelNama.setText(guru.getNama());
                TextView labelAlamat = (TextView) convertView
                        .findViewById(R.id.labelAlamat);
                labelAlamat.setText(guru.getAlamat());
            }
            return convertView;
        }
    }
}
