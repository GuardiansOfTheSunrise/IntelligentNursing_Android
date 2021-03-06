package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.entity.ServerResponse;
import com.gots.intelligentnursing.tools.LogUtil;
import com.gots.intelligentnursing.business.CrashLogManager.LogFile;
import com.gots.intelligentnursing.business.IServerConnection.ISystemOperate;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author zhqy
 * @date 2018/6/13
 */

public class SystemChecker implements Runnable {

    private static final String TAG = "SystemChecker";

    @Override
    public void run() {
        LogUtil.i(TAG, "SystemChecker run.");
        checkCrashLog();
        checkUpdate();
    }

    private void checkCrashLog() {
        CrashLogManager manager = CrashLogManager.getInstance(null);
        List<LogFile> logFiles = manager.getLogFiles();
        ISystemOperate systemOperate = RetrofitHelper.getInstance().system();
        if (logFiles != null) {
            LogUtil.i(TAG, logFiles.size() + " crash log file was found.");
            for (LogFile file : logFiles) {
                String log = file.getContent();
                if (log != null) {
                    LogUtil.i(TAG, "Log file content is not null, upload to server.");
                    systemOperate.uploadExceptionLog(log)
                            .subscribeOn(Schedulers.io())
                            .doOnNext(ServerResponse::checkSuccess)
                            .subscribe(
                                    resp -> file.delete(),
                                    throwable -> LogUtil.logExceptionStackTrace(TAG, throwable)
                            );
                }
            }
        }
    }

    private void checkUpdate() {

    }
}
