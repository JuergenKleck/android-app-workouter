package email.kleck.android.workouter.ui.settings;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import email.kleck.android.workouter.business.DataIntegrator;

class SettingsListener {

    private ImageView foreground;
    private ImageView background;
    private TextView textSample;

    SettingsListener(ImageView foreground, ImageView background, TextView textSample) {
        this.foreground = foreground;
        this.background = background;
        this.textSample = textSample;
    }

    void updateForeground() {
        foreground.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cTextR, DataIntegrator.localAppStorage.settings.cTextG, DataIntegrator.localAppStorage.settings.cTextB));
    }

    void updateBackground() {
        background.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cBackR, DataIntegrator.localAppStorage.settings.cBackG, DataIntegrator.localAppStorage.settings.cBackB));
    }

    void updateTextSample() {
        textSample.setTextSize(Integer.valueOf(DataIntegrator.localAppStorage.settings.textSize).floatValue());
    }

    SeekBar.OnSeekBarChangeListener listenerTextR = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cTextR = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateForeground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerTextG = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cTextG = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateForeground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerTextB = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cTextB = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateForeground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerBackR = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cBackR = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateBackground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerBackG = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cBackG = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateBackground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerBackB = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.cBackB = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateBackground();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener listenerTextSize = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            DataIntegrator.localAppStorage.settings.textSize = i;
            DataIntegrator.localAppStorage.dataChanged = true;
            updateTextSample();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    Switch.OnCheckedChangeListener listenerSwitch = (buttonView, isChecked) -> {
        DataIntegrator.localAppStorage.settings.kg = isChecked;
        DataIntegrator.localAppStorage.dataChanged = true;
    };

}
