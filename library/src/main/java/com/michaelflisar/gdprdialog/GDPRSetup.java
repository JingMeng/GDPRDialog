package com.michaelflisar.gdprdialog;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;

public class GDPRSetup implements Parcelable {

    private String mPolicyLink = null;
    private boolean mHasPaidVersion = false;
    private boolean mAllowNonPersonalisedForPaidVersion = false;
    private boolean mAllowNoConsent = false;
    private GDPRNetwork mAdNetworks[];

    private boolean mExplicitAgeConfirmation = false;
    private boolean mExplicitNonPersonalisedConfirmation = false;
    private boolean mNoToolbarTheme = false;
    private boolean mCheckRequestLocation = false;
    private boolean mUseBottomSheet = false;
    private boolean mForceSelection = false;

    public GDPRSetup(GDPRNetwork... adNetworks) {
        if (adNetworks == null || adNetworks.length == 0) {
            throw new RuntimeException("At least one ad network must be provided, otherwise this setup does not make any sense.");
        }
        mAdNetworks = adNetworks;
    }

    public GDPRSetup withPrivacyPolicy(String policyLink) {
        if (!policyLink.startsWith("https://") && !policyLink.startsWith("http://")) {
            policyLink = "http://" + policyLink;
        }
        mPolicyLink = policyLink;
        return this;
    }

    public GDPRSetup withPrivacyPolicy(Context context, int policyLink) {
        return withPrivacyPolicy(context.getString(policyLink));
    }

    public GDPRSetup withPaidVersion(boolean alsoProvideNonPersonalisedOption) {
        mHasPaidVersion = true;
        mAllowNonPersonalisedForPaidVersion = alsoProvideNonPersonalisedOption;
        return this;
    }

    public GDPRSetup withAllowNoConsent(boolean allowNoConsent) {
        mAllowNoConsent = allowNoConsent;
        return this;
    }

    public GDPRSetup withExplicitAgeConfirmation(boolean explicitAgeConfirmation) {
        mExplicitAgeConfirmation = explicitAgeConfirmation;
        return this;
    }

    public GDPRSetup withExplicitNonPersonalisedConfirmation(boolean explicitNonPersonalisedConfirmation) {
        mExplicitNonPersonalisedConfirmation = explicitNonPersonalisedConfirmation;
        return this;
    }

    public GDPRSetup withNoToolbarTheme(boolean noToolbarTheme) {
        mNoToolbarTheme = noToolbarTheme;
        return this;
    }

    public GDPRSetup withCheckRequestLocation(boolean checkRequestLocation) {
        mCheckRequestLocation = checkRequestLocation;
        return this;
    }

    public GDPRSetup withBottomSheet(boolean useBottomSheet) {
        mUseBottomSheet = useBottomSheet;
        return this;
    }

    public GDPRSetup withForceSelection(boolean forceSelection) {
        mForceSelection = forceSelection;
        return this;
    }

    // ----------------
    // Functions
    // ----------------

    public final String getNetworksCommaSeperated(Context context, boolean withLinks) {
        String networks = withLinks ? mAdNetworks[0].getHtmlLink() : mAdNetworks[0].getName();
        String innerSep = context.getString(R.string.gdpr_list_seperator);
        String lastSep = context.getString(R.string.gdpr_last_list_seperator);
        String sep;
        for (int i = 1; i < mAdNetworks.length; i++) {
            sep = i == mAdNetworks.length - 1 ? lastSep : innerSep;
            if (withLinks) {
                networks += sep + mAdNetworks[i].getHtmlLink();
            } else {
                networks += sep + mAdNetworks[i].getName();
            }
        }
        return networks;
    }

    public final String policyLink() {
        return mPolicyLink;
    }

    public final GDPRNetwork[] networks() {
        return mAdNetworks;
    }

    public final boolean hasPaidVersion() {
        return mHasPaidVersion;
    }

    public final boolean allowNonPersonalisedForPaidVersion() {
        return mAllowNonPersonalisedForPaidVersion;
    }

    public final boolean allowNoConsent() {
        return mAllowNoConsent;
    }

    public final boolean allowAnyNoConsent() {
        return mAllowNoConsent  || mAllowNonPersonalisedForPaidVersion;
    }

    public final boolean explicitAgeConfirmation() {
        return mExplicitAgeConfirmation;
    }

    public final boolean explicitNonPersonalisedConfirmation() {
        return mExplicitNonPersonalisedConfirmation;
    }

    public final boolean noToolbarTheme() {
        return mNoToolbarTheme;
    }

    public final boolean useBottomSheet() {
        return mUseBottomSheet;
    }

    public final boolean checkRequestLocation() {
        return mCheckRequestLocation;
    }

    public final boolean forceSelection() {
        return mForceSelection;
    }

    public final boolean containsAdNetwork() {
        for (GDPRNetwork network : mAdNetworks) {
            if (network.isAdNetwork()) {
                return true;
            }
        }
        return false;
    }

    public HashSet<String> getNetworkTypes() {
        HashSet<String> uniqueTypes = new HashSet<>();
        for (GDPRNetwork network : mAdNetworks) {
            uniqueTypes.add(network.getType());
        }
        return uniqueTypes;
    }

    public String getNetworkTypesCommaSeperated(Context context) {
        HashSet<String> uniqueTypes = getNetworkTypes();

        String innerSep = context.getString(R.string.gdpr_list_seperator);
        String lastSep = context.getString(R.string.gdpr_last_list_seperator);
        String sep;

        String types = "";
        int i = 0;
        for (String type : uniqueTypes) {
            if (i == 0) {
                types = type;
            } else {
                sep = i == uniqueTypes.size() - 1 ? lastSep : innerSep;
                types += sep + type;
            }
            i++;
        }

        return types;
    }

    // ----------------
    // Parcelable
    // ----------------

    public GDPRSetup(Parcel in) {
        mPolicyLink = in.readString();
        mHasPaidVersion = in.readByte() == 1;
        mAllowNonPersonalisedForPaidVersion = in.readByte() == 1;
        mAllowNoConsent = in.readByte() == 1;
        Parcelable[] adNetworks = in.readParcelableArray(GDPRNetwork.class.getClassLoader());
        mAdNetworks = new GDPRNetwork[adNetworks.length];
        for (int i = 0; i < adNetworks.length; i++) {
            mAdNetworks[i] = (GDPRNetwork)adNetworks[i];
        }
        mExplicitAgeConfirmation = in.readByte() == 1;
        mExplicitNonPersonalisedConfirmation = in.readByte() == 1;
        mNoToolbarTheme = in.readByte() == 1;
        mCheckRequestLocation = in.readByte() == 1;
        mUseBottomSheet = in.readByte() == 1;
        mForceSelection = in.readByte() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPolicyLink);
        dest.writeInt(mHasPaidVersion ? (byte) 1 : 0);
        dest.writeInt(mAllowNonPersonalisedForPaidVersion ? (byte) 1 : 0);
        dest.writeInt(mAllowNoConsent ? (byte) 1 : 0);
        dest.writeParcelableArray(mAdNetworks, 0);
        dest.writeByte(mExplicitAgeConfirmation ? (byte) 1 : 0);
        dest.writeByte(mExplicitNonPersonalisedConfirmation ? (byte) 1 : 0);
        dest.writeByte(mNoToolbarTheme ? (byte) 1 : 0);
        dest.writeByte(mCheckRequestLocation ? (byte) 1 : 0);
        dest.writeByte(mUseBottomSheet ? (byte) 1 : 0);
        dest.writeByte(mForceSelection ? (byte) 1 : 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GDPRSetup createFromParcel(Parcel in) {
            return new GDPRSetup(in);
        }

        public GDPRSetup[] newArray(int size) {
            return new GDPRSetup[size];
        }
    };
}
