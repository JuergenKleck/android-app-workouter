package email.kleck.android.workouter.ui.settings;

import android.Manifest;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import email.kleck.android.workouter.R;
import email.kleck.android.workouter.business.DataIntegrator;
import email.kleck.android.workouter.ui.BaseFragment;

public class SettingFragment extends BaseFragment {

    private Dialog addDialog;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView mText;
    private boolean hasPathExplorer;
    private boolean isImport;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onPermissionResult(String permission, boolean granted) {
        if (!hasPathExplorer && granted && permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activatePathExplorer();
            }
        } else if (isImport && granted && permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            doImport();
        } else if (granted && permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            doExport();
        }
    }

    @Override
    public void onFragmentCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        SettingViewModel viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        final TextView textView = root.findViewById(R.id.text_settings);
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        toggleFAB(viewGroup.getRootView().findViewById(R.id.fab), false);

        root.findViewById(R.id.btn_settings_importexport).setOnClickListener(view -> addDialog.show());

        addDialog = new Dialog(this.getContext(), R.style.Theme_AppCompat_Light_Dialog);
        addDialog.setContentView(R.layout.dialog_import_export);
        addDialog.findViewById(R.id.btn_import).setOnClickListener(onBtnImport);
        addDialog.findViewById(R.id.btn_export).setOnClickListener(onBtnExport);

        mText = addDialog.findViewById(R.id.import_export_ac_path);

        isImport = false;
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activatePathExplorer();
            }
            hasPathExplorer = true;
        } else {
            hasPathExplorer = false;
        }
        List<String> strings = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, strings);
        mText.setAdapter(adapter);
        mText.setText("/mnt/sdcard");

        SettingsListener listeners = new SettingsListener(root.findViewById(R.id.foreground_preview),
                root.findViewById(R.id.background_preview),
                root.findViewById(R.id.textViewTsSample));

        SeekBar seekFgR = root.findViewById(R.id.seek_fg_r);
        SeekBar seekFgG = root.findViewById(R.id.seek_fg_g);
        SeekBar seekFgB = root.findViewById(R.id.seek_fg_b);

        SeekBar seekBgR = root.findViewById(R.id.seek_bg_r);
        SeekBar seekBgG = root.findViewById(R.id.seek_bg_g);
        SeekBar seekBgB = root.findViewById(R.id.seek_bg_b);

        SeekBar seekTextSize = root.findViewById(R.id.seek_textsize);

        Switch swtch = root.findViewById(R.id.kg_or_lb_switch);

        listeners.updateBackground();
        listeners.updateForeground();
        listeners.updateTextSample();

        seekFgR.setProgress(DataIntegrator.localAppStorage.settings.cTextR);
        seekFgG.setProgress(DataIntegrator.localAppStorage.settings.cTextG);
        seekFgB.setProgress(DataIntegrator.localAppStorage.settings.cTextB);
        seekBgR.setProgress(DataIntegrator.localAppStorage.settings.cBackR);
        seekBgG.setProgress(DataIntegrator.localAppStorage.settings.cBackG);
        seekBgB.setProgress(DataIntegrator.localAppStorage.settings.cBackB);
        seekTextSize.setProgress(DataIntegrator.localAppStorage.settings.textSize);
        if (DataIntegrator.localAppStorage.settings.kg) {
            swtch.toggle();
        }

        seekFgR.setOnSeekBarChangeListener(listeners.listenerTextR);
        seekFgG.setOnSeekBarChangeListener(listeners.listenerTextG);
        seekFgB.setOnSeekBarChangeListener(listeners.listenerTextB);

        seekBgR.setOnSeekBarChangeListener(listeners.listenerBackR);
        seekBgG.setOnSeekBarChangeListener(listeners.listenerBackG);
        seekBgB.setOnSeekBarChangeListener(listeners.listenerBackB);

        seekTextSize.setOnSeekBarChangeListener(listeners.listenerTextSize);

        swtch.setOnCheckedChangeListener(listeners.listenerSwitch);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void activatePathExplorer() {
        mText.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SLASH) {
                String text = mText.getText().toString();
                adapter.clear();
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(text))) {
                    for (Path path : directoryStream) {
                        adapter.add(path.toString());
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
    }

    private View.OnClickListener onBtnImport = new View.OnClickListener() {
        public void onClick(View v) {
            isImport = true;
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, true)) {
                doImport();
            }
        }
    };

    private View.OnClickListener onBtnExport = new View.OnClickListener() {
        public void onClick(View v) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, true)) {
                doExport();
            }
        }
    };

    private void doImport() {
        String path = ((AutoCompleteTextView) addDialog.findViewById(R.id.import_export_ac_path)).getText().toString();
        DataIntegrator.localAppStorage = DataIntegrator.readData(getContext(), path);
        addDialog.dismiss();
    }

    private void doExport() {
        String path = ((AutoCompleteTextView) addDialog.findViewById(R.id.import_export_ac_path)).getText().toString();
        DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, path);
        addDialog.dismiss();
    }
}