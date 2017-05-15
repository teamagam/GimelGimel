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
    private long mTimeoutMillis;
    private boolean mIsDisconnectTaskScheduled;
    private ConnectivityStatus mCurrentStatus;

    public PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus initStatus, long timeoutMillis) {
        mInnerRepo = SubjectRepository.createReplayCount(1);
        mTimer = new Timer();
        mTimeoutMillis = timeoutMillis;
        mIsDisconnectTaskScheduled = false;
        updateStatus(initStatus);
    }

    @Override
    public synchronized void setStatus(ConnectivityStatus newStatus) {
        if (isConnectedUpdate(newStatus)) {
            cancelDisconnectTask();
            updateStatus(ConnectivityStatus.CONNECTED);
        } else if (currentlyConnected() && noDisconnectTaskScheduledYet()) {
            scheduleDisconnectTask();
        }
    }

    @Override
    public Observable<ConnectivityStatus> getObservable() {
        return mInnerRepo.getObservable();
    }

    private synchronized void updateStatus(ConnectivityStatus status) {
        if (mCurrentStatus != status) {
            mCurrentStatus = status;
            mInnerRepo.add(mCurrentStatus);
        }
    }

    private boolean isConnectedUpdate(ConnectivityStatus newStatus) {
        return newStatus == ConnectivityStatus.CONNECTED;
    }

    private void cancelDisconnectTask() {
        mTimer.cancel();
        mTimer.purge();
        mIsDisconnectTaskScheduled = false;
    }

    private boolean currentlyConnected() {
        return mCurrentStatus != ConnectivityStatus.DISCONNECTED;
    }

    private boolean noDisconnectTaskScheduledYet() {
        return !mIsDisconnectTaskScheduled;
    }

    private void scheduleDisconnectTask() {
        mTimer = new Timer();
        mTimer.schedule(new DisconnectTask(), mTimeoutMillis);
        mIsDisconnectTaskScheduled = true;
    }

    private class DisconnectTask extends TimerTask {
        @Override
        public void run() {
            updateStatus(ConnectivityStatus.DISCONNECTED);
            mIsDisconnectTaskScheduled = false;
        }
    }
}

