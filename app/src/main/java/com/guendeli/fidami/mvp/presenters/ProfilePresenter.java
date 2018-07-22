package com.guendeli.fidami.mvp.presenters;

public interface ProfilePresenter extends BasePresenter {

    public void saveData(String name, String surname, String address, String bloodType, String sex,
                         String additional, String rhType, String age, String weight);

    public void saveData(String address, String additional);
}
