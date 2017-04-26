package lwq.com.recyclerviewdemo.adapter;

import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的动画设置
 */

public class MyItemAnimator extends SimpleItemAnimator {

    private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
    private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
    private ArrayList<MoveInfo> mPendingMoves = new ArrayList<>();
    private ArrayList<ChangeInfo> mPendingChanges = new ArrayList<>();

    ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<>();
    ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<>();
    ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<>();

    ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
    ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
    ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
    ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();
    private boolean DEBUG = false;

    /**
     * 动画移动信息
     */
    private class MoveInfo {
        public RecyclerView.ViewHolder holder;

        public int fromX, fromY, toX, toY;

        public MoveInfo(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    /**
     * 动画改变信息
     */
    private class ChangeInfo {
        public RecyclerView.ViewHolder oldHolder, newHolder;
        public int fromX, fromY, toX, toY;

        public ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }

        public ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
            this(oldHolder, newHolder);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        @Override
        public String toString() {
            return "ChangeInfo{" +
                    "oldHolder=" + oldHolder +
                    ", newHolder=" + newHolder +
                    ", fromX=" + fromX +
                    ", fromY=" + fromY +
                    ", toX=" + toX +
                    ", toY=" + toY +
                    '}';
        }
    }

    /**
     * 当我们调用Adapter.notifyItemInsert()时会触发该方法，
     * 该方法有一个boolean类型的返回值，返回值表示：runPendingAnimations是否可以在下一个时机去执行。
     * 所以当我们定制动画时，这个方法要返回true。
     *
     * @param holder
     * @return
     */
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        resetAnimation(holder);
        mPendingRemovals.add(holder);
        return true;
    }

    /**
     * 重置动画
     *
     * @param holder
     */
    private void resetAnimation(RecyclerView.ViewHolder holder) {
        AnimatorCompatHelper.clearInterpolator(holder.itemView);
        endAnimation(holder);
    }

    /**
     * add时的动画，当我们调用Adapter.notifyItemInsert()时会触发该方法，
     * 该方法有一个boolean类型的返回值，返回值表示：runPendingAnimations是否可以在下一个时机去执行。
     * 所以当我们定制动画时，这个方法要返回true。
     *
     * @param holder
     * @return
     */
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        resetAnimation(holder);
        //View下移
        ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getWidth());
        mPendingAdditions.add(holder);
        return true;
    }

    /**
     * 动画移动
     * 当我们调用Adapter.notifyItemInsert()时会触发该方法，
     * 该方法有一个boolean类型的返回值，返回值表示：runPendingAnimations是否可以在下一个时机去执行。
     * 所以当我们定制动画时，这个方法要返回true。
     *
     * @param holder
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        View view = holder.itemView;
        fromX += ViewCompat.getTranslationX(holder.itemView);
        fromY += ViewCompat.getTranslationY(holder.itemView);
        resetAnimation(holder);
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        if (deltaX == 0 && deltaY == 0) {
            //动画结束调用
            dispatchMoveFinished(holder);
            return false;
        }
        if (deltaX != 0) {
            //View平移
            ViewCompat.setTranslationX(view, -deltaX);
        }
        if (deltaY != 0) {
            //View下移
            ViewCompat.setTranslationY(view, -deltaY);
        }
        mPendingMoves.add(new MoveInfo(holder, fromX, fromY, toX, toY));
        return true;
    }

    /**
     * 动画改变
     * 当我们调用Adapter.notifyItemInsert()时会触发该方法，
     * 该方法有一个boolean类型的返回值，返回值表示：runPendingAnimations是否可以在下一个时机去执行。
     * 所以当我们定制动画时，这个方法要返回true。
     *
     * @param oldHolder
     * @param newHolder
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        if (oldHolder == newHolder) {
            return animateMove(oldHolder, fromX, fromY, toX, toY);
        }
        float prevTranslationX = ViewCompat.getTranslationX(oldHolder.itemView);
        float prevTranslationY = ViewCompat.getTranslationY(oldHolder.itemView);
        float prevAlpa = ViewCompat.getAlpha(oldHolder.itemView);
        resetAnimation(oldHolder);
        int deltaX = (int) (toX - fromX - prevTranslationX);
        int deltaY = (int) (toY - fromY - prevTranslationY);
        ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
        ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);
        ViewCompat.setAlpha(oldHolder.itemView, prevAlpa);
        if (newHolder != null) {
            resetAnimation(newHolder);
            ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
            ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
            ViewCompat.setTranslationX(newHolder.itemView, -newHolder.itemView.getWidth());
        }
        mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
        return true;
    }

    /**
     * 当有动画要执行的时候调用。这里需要说明一点，当我们去add一个item时，动画可能不是立即去执行的，
     * 这种机制可以让ItemAnimator一个个的添加，然后一块去执行。
     * <p>
     * 统筹RecyclerView中所有的动画，统一启动执行
     */
    @Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean movesPending = !mPendingMoves.isEmpty();
        boolean changePending = !mPendingChanges.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        //没有动画时
        if (!removalsPending && !movesPending && !additionsPending && !changePending) {
            return;
        }

        for (RecyclerView.ViewHolder holder : mPendingRemovals) {
            animateRemoveImpl(holder);
        }

        mPendingRemovals.clear();
        //移动动画执行
        if (movesPending) {
            final ArrayList<MoveInfo> moves = new ArrayList<>();
            moves.addAll(mPendingMoves);
            mMovesList.add(moves);
            mPendingMoves.clear();
            Runnable mover = new Runnable() {
                @Override
                public void run() {
                    for (MoveInfo moveInfo : moves) {
                        animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                    }
                    moves.clear();
                    mMovesList.remove(moves);
                }
            };
            if (removalsPending) {
                View view = moves.get(0).holder.itemView;
                ViewCompat.postOnAnimationDelayed(view, mover, getRemoveDuration());
            } else {
                mover.run();
            }
        }
        //数据改变动画执行
        if (changePending) {
            final ArrayList<ChangeInfo> changeInfos = new ArrayList<>();
            changeInfos.addAll(mPendingChanges);
            mChangesList.add(changeInfos);
            mPendingChanges.clear();
            Runnable changer = new Runnable() {
                @Override
                public void run() {
                    for (ChangeInfo changeInfo : changeInfos) {
                        animateChangeImpl(changeInfo);
                    }
                    changeInfos.clear();
                    mChangesList.remove(changeInfos);
                }
            };
            if (removalsPending) {
                RecyclerView.ViewHolder holder = changeInfos.get(0).oldHolder;
                ViewCompat.postOnAnimationDelayed(holder.itemView, changer, getRemoveDuration());
            } else {
                changer.run();
            }
        }
        if (additionsPending) {
            final ArrayList<RecyclerView.ViewHolder> additons = new ArrayList<>();
            additons.addAll(mPendingAdditions);
            mAdditionsList.add(additons);
            mPendingAdditions.clear();
            Runnable adder = new Runnable() {
                @Override
                public void run() {
                    for (RecyclerView.ViewHolder holder : additons) {
                        animateAddImpl(holder);
                    }
                    additons.clear();
                    mAdditionsList.remove(additons);
                }
            };
            if (removalsPending || movesPending || changePending) {
                long removeDuration = removalsPending ? getRemoveDuration() : 0;
                long moveDuration = movesPending ? getMoveDuration() : 0;
                long changeDuration = changePending ? getChangeDuration() : 0;
                long totalDelay = removeDuration + Math.max(moveDuration, changeDuration);
                View view = additons.get(0).itemView;
                ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
            } else {
                adder.run();
            }
        }
    }

    /**
     * 实施数据增加时的动画
     *
     * @param holder
     */
    private void animateAddImpl(final RecyclerView.ViewHolder holder) {
        View view = holder.itemView;
        final ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animatorCompat.translationX(0).setDuration(getAddDuration()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchAddStarting(holder);
            }

            @Override
            public void onAnimationCancel(View view) {
                ViewCompat.setAlpha(view, 1);
            }

            @Override
            public void onAnimationEnd(View view) {
                animatorCompat.setListener(null);
                dispatchAddFinished(holder);
                mAddAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    /**
     * 实施数据刷新时的动画
     *
     * @param changeInfo
     */
    private void animateChangeImpl(final ChangeInfo changeInfo) {
        RecyclerView.ViewHolder holder = changeInfo.oldHolder;
        View view = holder == null ? null : holder.itemView;
        final RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
        final View newView = newHolder != null ? newHolder.itemView : null;
        if (view != null) {
            final ViewPropertyAnimatorCompat oldViewAnim = ViewCompat.animate(view).setDuration(getChangeDuration());
            mChangeAnimations.add(changeInfo.oldHolder);
            oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
            oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
            oldViewAnim.translationX(view.getWidth()).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchChangeStarting(changeInfo.oldHolder, true);
                }

                @Override
                public void onAnimationEnd(View view) {
                    oldViewAnim.setListener(null);
                    ViewCompat.setAlpha(view, 1);
                    ViewCompat.setTranslationX(view, 0);
                    ViewCompat.setTranslationY(view, 0);
                    dispatchChangeFinished(changeInfo.oldHolder, true);
                    mChangeAnimations.remove(changeInfo.oldHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
        if (newView != null) {
            final ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(newView);
            mChangeAnimations.add(changeInfo.newHolder);
            animatorCompat.translationX(0).translationY(0).setDuration(getChangeDuration()).alpha(1).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchChangeStarting(changeInfo.newHolder, false);
                }

                @Override
                public void onAnimationEnd(View view) {
                    animatorCompat.setListener(null);
                    ViewCompat.setAlpha(newView, 1);
                    ViewCompat.setTranslationX(newView, 0);
                    ViewCompat.setTranslationY(newView, 0);
                    dispatchChangeFinished(changeInfo.newHolder, false);
                    mChangeAnimations.remove(changeInfo.newHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }

    /**
     * 实施数据删除时的动画
     *
     * @param holder
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     */
    private void animateMoveImpl(final RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        View view = holder.itemView;
        final int deltaX = toX - fromX;
        final int deltaY = toY - fromY;
        if (deltaX != 0) {
            ViewCompat.animate(view).translationX(0);
        }
        if (deltaY != 0) {
            ViewCompat.animate(view).translationY(0);
        }
        final ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(view);
        animatorCompat.rotationXBy(180);
        mMoveAnimations.add(holder);
        animatorCompat.setDuration(getMoveDuration()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchAddStarting(holder);
            }

            @Override
            public void onAnimationCancel(View view) {
                if (deltaX != 0) {
                    ViewCompat.setTranslationX(view, 0);
                }
                if (deltaY != 0) {
                    ViewCompat.setTranslationY(view, 0);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                animatorCompat.setListener(null);
                ViewCompat.setRotationX(view, 0);
                dispatchAddFinished(holder);
                mMoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    /**
     * 实施数据删除时的动画
     *
     * @param holder
     */
    private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        //首先得到ItemVew
        final View view = holder.itemView;
        //开启一个属性动画
        final ViewPropertyAnimatorCompat animatorCompat = ViewCompat.animate(view);
        //加入到要删除的View中
        mRemoveAnimations.add(holder);
        animatorCompat.setDuration(getRemoveDuration()).translationX(view.getWidth()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                //当动画结束时，要将监听视为空
                animatorCompat.setListener(null);
                //因为有复用布吉的问题，所以你将控件删除的时候需要将他还原，不然会出现重复问题；
                ViewCompat.setTranslationX(view, 0);
                dispatchAddFinished(holder);
                mRemoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    /**
     * 所有动画结束时调用
     */
    private void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    /**
     * 结束某一动画
     *
     * @param item
     */
    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        View view = item.itemView;
        ViewCompat.animate(view).cancel();
        for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
            MoveInfo moveInfo = mPendingMoves.get(i);
            if (moveInfo.holder == item) {
                ViewCompat.setTranslationY(view, 0);
                ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(item);
                mPendingMoves.remove(i);
            }
        }
        endChangeAnimation(mPendingChanges, item);
        if (mPendingRemovals.remove(item)) {
            ViewCompat.setAlpha(view, 1);
            dispatchRemoveFinished(item);
        }
        if (mPendingAdditions.remove(item)) {
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
        }
        for (int i = mChangesList.size() - 1; i >= 0; i--) {
            ArrayList<ChangeInfo> changeInfos = mChangesList.get(i);
            endChangeAnimation(changeInfos, item);
            if (changeInfos.isEmpty()) {
                mChangesList.remove(i);
            }
        }
        for (int i = mMovesList.size() - 1; i >= 0; i--) {
            ArrayList<MoveInfo> moveInfos = mMovesList.get(i);
            for (int j = moveInfos.size() - 1; j >= 0; j--) {
                MoveInfo moveInfo = moveInfos.get(j);
                if (moveInfo.holder == item) {
                    ViewCompat.setTranslationY(view, 0);
                    ViewCompat.setTranslationX(view, 0);
                    dispatchMoveFinished(item);
                    moveInfos.remove(j);
                    if (moveInfos.isEmpty()) {
                        mMovesList.remove(i);
                    }
                    break;
                }
            }
        }
        for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
            ArrayList<RecyclerView.ViewHolder> arrayList = mAdditionsList.get(i);
            if (arrayList.remove(item)) {
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                if (arrayList.isEmpty()) {
                    mAdditionsList.remove(i);
                }
            }
        }
        if (mRemoveAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in " + "mRemoveAnimations list");
        }

        if (mAddAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in " + "mAddAnimations list");
        }

        if (mChangeAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in " + "mChangeAnimations list");
        }

        if (mMoveAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in " + "mMoveAnimations list");
        }
        dispatchFinishedWhenDone();
    }

    /**
     * 结束所有动画
     *
     * @param mPendingChanges
     * @param item
     */
    private void endChangeAnimation(ArrayList<ChangeInfo> mPendingChanges, RecyclerView.ViewHolder item) {
        for (int i = mPendingChanges.size() - 1; i > 0; i--) {
            ChangeInfo changeInfo = mPendingChanges.get(i);
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    mPendingChanges.remove(changeInfo);
                }
            }
        }
    }

    /**
     * 如果必要，结束所有动画
     *
     * @param changeInfo
     */
    private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }

    /**
     * 如果必要，结束所有动画
     *
     * @param changeInfo
     * @param item
     * @return
     */
    private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder item) {
        boolean oldItem = false;
        if (changeInfo.newHolder == item) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder == item) {
            changeInfo.oldHolder = null;
            oldItem = true;
        } else {
            return false;
        }
        ViewCompat.setAlpha(item.itemView, 1);
        ViewCompat.setTranslationX(item.itemView, 0);
        ViewCompat.setTranslationY(item.itemView, 0);
        dispatchChangeFinished(item, oldItem);
        return true;
    }

    /**
     * 结束所有动画
     */
    @Override
    public void endAnimations() {
        int count = mPendingMoves.size();
        for (int i = count - 1; i >= 0; i--) {
            MoveInfo info = mPendingMoves.get(i);
            View view = info.holder.itemView;
            ViewCompat.setTranslationY(view, 0);
            ViewCompat.setTranslationX(view, 0);
            dispatchMoveFinished(info.holder);
            mPendingMoves.remove(i);
        }
        count = mPendingRemovals.size();
        for (int i = count - 1; i >= 0; i--) {
            RecyclerView.ViewHolder viewHolder = mPendingRemovals.get(i);
            dispatchRemoveFinished(viewHolder);
            mPendingRemovals.remove(i);
        }
        count = mPendingAdditions.size();
        for (int i = count - 1; i >= 0; i--) {
            RecyclerView.ViewHolder viewHolder = mPendingAdditions.get(i);
            View view = viewHolder.itemView;
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(viewHolder);
            mPendingAdditions.remove(i);
        }
        count = mPendingChanges.size();
        for (int i = count - 1; i >= 0; i--) {
            endChangeAnimationIfNecessary(mPendingChanges.get(i));
        }
        mPendingChanges.clear();
        if (!isRunning()) {
            return;
        }

        int listCount = mMovesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<MoveInfo> moveInfos = mMovesList.get(i);
            count = moveInfos.size();
            for (int j = count - 1; j >= 0; j--) {
                MoveInfo moveInfo = moveInfos.get(j);
                RecyclerView.ViewHolder viewHolder = moveInfo.holder;
                View view = viewHolder.itemView;
                ViewCompat.setTranslationY(view, 0);
                ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(moveInfo.holder);
                moveInfos.remove(j);
                if (moveInfos.isEmpty()) {
                    mMovesList.remove(moveInfos);
                }
            }
        }
        listCount = mAdditionsList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<RecyclerView.ViewHolder> arrayList = mAdditionsList.get(i);
            count = arrayList.size();
            for (int j = count - 1; j >= 0; j--) {
                RecyclerView.ViewHolder holder = arrayList.get(j);
                View view = holder.itemView;
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(holder);
                arrayList.remove(j);
                if (arrayList.isEmpty()) {
                    mAdditionsList.remove(arrayList);
                }
            }
        }
        listCount = mChangesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<ChangeInfo> changeInfos = mChangesList.get(i);
            count = changeInfos.size();
            for (int j = count - 1; j >= 0; j--) {
                endChangeAnimationIfNecessary(changeInfos.get(j));
                if (changeInfos.isEmpty()) {
                    mChangesList.remove(changeInfos);
                }
            }
        }
        cancelAll(mRemoveAnimations);
        cancelAll(mMoveAnimations);
        cancelAll(mAddAnimations);
        cancelAll(mChangeAnimations);

        dispatchAnimationsFinished();
    }

    /**
     * 取消所有动画
     *
     * @param mRemoveAnimations
     */
    private void cancelAll(ArrayList<RecyclerView.ViewHolder> mRemoveAnimations) {
        for (int i = mRemoveAnimations.size() - 1; i >= 0; i--) {
            ViewCompat.animate(mRemoveAnimations.get(i).itemView).cancel();
        }
    }

    /**
     * 返回当前是否有动画需要执行。
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return (!mPendingAdditions.isEmpty() ||
                !mPendingChanges.isEmpty() ||
                !mPendingMoves.isEmpty() ||
                !mPendingRemovals.isEmpty() ||
                !mMoveAnimations.isEmpty() ||
                !mRemoveAnimations.isEmpty() ||
                !mAddAnimations.isEmpty() ||
                !mChangeAnimations.isEmpty() ||
                !mMovesList.isEmpty() ||
                !mAdditionsList.isEmpty() ||
                !mChangesList.isEmpty());
    }

    /**
     * 当RecyclerView中的item在屏幕上由可见变为不可见时调用此方法
     *
     * @param viewHolder
     * @param preLayoutInfo
     * @param postLayoutInfo
     * @return
     */
    @Override
    public boolean animateDisappearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    /**
     * 当RecyclerView中的item显示到屏幕上时调用此方法
     *
     * @param viewHolder
     * @param preLayoutInfo
     * @param postLayoutInfo
     * @return
     */
    @Override
    public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    /**
     * 当RecyclerView中的item状态发生改变时调用此方法(notifyItemChanged(position))
     *
     * @param oldHolder
     * @param newHolder
     * @param preInfo
     * @param postInfo
     * @return
     */
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo) {
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder, List<Object> payloads) {
        return !payloads.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads);
    }

    private class VpaListenerAdapter implements ViewPropertyAnimatorListener {

        VpaListenerAdapter() {

        }

        @Override
        public void onAnimationStart(View view) {

        }

        @Override
        public void onAnimationEnd(View view) {

        }

        @Override
        public void onAnimationCancel(View view) {

        }
    }
}
