package llanes.ezquerro.juan.sshd.setup;


import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import llanes.ezquerro.juan.sshd.R;
import llanes.ezquerro.juan.sshd.constants.SSHdConstants;

public class SSHdResources {
    private Context mContext;
    private File configFile;
    private File setupScript;
    private File authorizedKeys;

    public SSHdResources(Context context) {
        mContext = context;

        File binDir = mContext.getDir(SSHdConstants.SSHD_BIN_DIRECTORY, Application.MODE_PRIVATE);
        File dataDir = mContext.getDir(SSHdConstants.SSHD_DATA_DIRECTORY, Application.MODE_PRIVATE);

        if (!binDir.isDirectory()) binDir.mkdirs();
        if (!dataDir.isDirectory()) dataDir.mkdirs();

        configFile = new File(dataDir, SSHdConstants.SSHD_CONFIG);
        setupScript = new File(binDir, SSHdConstants.SSHD_SETUP_SCRIPT);
        authorizedKeys = new File(dataDir, SSHdConstants.SSHD_AUTHORIZED_KEYS);
    }

    public boolean areInstalled() {
        return configFile.exists() && setupScript.exists();
    }

    public boolean installSSHdFiles() {
        InputStream is;

        is = mContext.getResources().openRawResource(R.raw.config_setup);

        try {
            streamToFile(is, setupScript, false);
        } catch (IOException e) {
            return false;
        }

        is = mContext.getResources().openRawResource(R.raw.sshd_config);

        try {
            streamToFile(is, configFile, false);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean saveAuthorizedKey(String key) {
        FileWriter stmOut;
        try {
            stmOut = new FileWriter(authorizedKeys.getAbsolutePath(), false);
            stmOut.write(key);
            stmOut.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private void streamToFile(InputStream stm, File outFile, boolean append) throws IOException

    {
        int FILE_WRITE_BUFFER_SIZE = 1024;

        byte[] buffer = new byte[FILE_WRITE_BUFFER_SIZE];

        int bytecount;

        OutputStream stmOut = new FileOutputStream(outFile.getAbsolutePath(), append);

        while ((bytecount = stm.read(buffer)) > 0) {
            stmOut.write(buffer, 0, bytecount);
        }

        stmOut.close();
        stm.close();
    }

    public String getConfigFilePath() {
        return configFile.getAbsolutePath();
    }

    public String getSetupScriptPath() {
        return setupScript.getAbsolutePath();
    }

    public String getAuthKeysPath() {
        return authorizedKeys.getAbsolutePath();
    }
}
