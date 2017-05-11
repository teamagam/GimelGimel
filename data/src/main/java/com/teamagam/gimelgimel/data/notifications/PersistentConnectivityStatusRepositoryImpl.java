package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;

public class PersistentConnectivityStatusRepositoryImpl implements ConnectivityStatusRepository {

    private final SubjectRepository<ConnectivityStatus> mInnerRepo;
    private Timer mTimer;
    private TimerTask mDisconnectTask;
    private long mTimeoutMillis;
    private ConnectivityStatus mCurrentStatus;

    public PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus initStatus, long timeoutMillis) {
        mInnerRepo = SubjectRepository.createReplayCount(1);
        mTimer = new Timer();
        mDisconnectTask = new DisconnectTask();
        mTimeoutMillis = timeoutMillis;
        updateStatus(initStatus);
    }

    @Override
    public void setStatus(ConnectivityStatus status) {
        if (status == ConnectivityStatus.DISCONNECTED) {
            scheduleDisconnectTask();
        } else {
            cancelDisconnectTask();
            updateStatus(ConnectivityStatus.CONNECTED);
        }
    }

    @Override
    public Observable<ConnectivityStatus> getObservable() {
        return mInnerRepo.getObservable();
    }

    private void scheduleDisconnectTask() {
        mTimer = new Timer();
        mDisconnectTask = new DisconnectTask();
        mTimer.schedule(mDisconnectTask, mTimeoutMillis);
    }

    private void cancelDisconnectTask() {
        mTimer.cancel();
        mTimer.purge();
    }

    private synchronized void updateStatus(ConnectivityStatus status) {
        if (mCurrentStatus != status) {
            mCurrentStatus = status;
            mInnerRepo.add(mCurrentStatus);
        }
    }

    private class DisconnectTask extends TimerTask {
        @Override
        public void run() {
            updateStatus(ConnectivityStatus.DISCONNECTED);
        }
    }
}

