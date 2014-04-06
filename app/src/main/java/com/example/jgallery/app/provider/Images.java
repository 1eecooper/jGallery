package com.example.jgallery.app.provider;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class Images {

    private static File mRootPicDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
    private static ArrayList<String> mPicDirPaths;

    private static void setDirectoryList(File file) {
        File files [] = file.listFiles();
        for (File f: files) {
            if (f.isDirectory()) {
                mPicDirPaths.add(f.toString());
                setDirectoryList(f);
            }
        }
    }

    public static ArrayList<String> getImagesFromDevice() {
        mPicDirPaths = new ArrayList<String>();
        mPicDirPaths.add(mRootPicDir.toString());
        setDirectoryList(mRootPicDir);

        ArrayList<String> imagePaths = new ArrayList<String>();
        for (String str: mPicDirPaths) {
            File dir = new File(str);
            File [] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].toString().toLowerCase().endsWith(".jpg") ||
                        files[i].toString().toLowerCase().endsWith(".png")) {
                    imagePaths.add(files[i].toString());
                }
            }
        }
        return imagePaths;
    }

    public static ArrayList<String> getImagesFromUrl() {
        ArrayList<String> imagePaths = new ArrayList<String>();

        imagePaths.add("https://lh4.googleusercontent.com/XFNAy05M0TDWAO5vT10JqGts2hHIyNDdBgHvNPaCig=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/CnIk5I12e6u38jsMCxJdn552HRasjmMWj_W8uRnmBA=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/YMhH5_dq5sBiaTu_BJKZ9OtFPmxRdRj-tXsqus8g3w=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/xe9f5zHlICq0BZ3Xzabz-jVMM2pWeCd5Fo9L6YnMhA=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/rwllAqHBVIifMTyoQo3X2mhDlVn6hdl1VPqyhQln1Q=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/2KizjL8Yz9vgSzOQbMX4e0fLMkfF77ia97L8huYP7A=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/o6PTBtxYX96-KL3tO09eWztoIH91_cFsuxX9IGbB_jM=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/EEFwVuvz6vfKxJyReojchk5lb-jEWrU7QHb4c3YLxw=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/PPcF-xg0LbZPPpf6adAD-eD0pLrHQ1QrzveaylLuBA=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/RBsljkU1Y3UbVT4_pdLGutfYFfzZtIEkFynQDbOgHA=w338-h211-p-no");

        imagePaths.add("https://lh5.googleusercontent.com/7apZUS06PKsf069HjHef4msZZjxY31zbV0mVYi1yzg=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/VSLHp7mxQ38CilXMbJKzDDHEVODfCs9_Qf7ntnz4Zw=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/7UDvtM4GCMHfIP1_xIrvYHnMHiHgl71Vk4iz-XJGYg=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/BS4UHbRjnoFWzhgYmgqvezH5oBjatY2a1yDLLtSMSA=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/wbX--4FE0ijCwTI5GBRXtcz7mBBD5fIeC2doPind_A=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/5O_dz53c8Gn7hqtl5IEoOZNLH4_5SWG-gNeOEd0IBw=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/e5D28o69d3MStCYO2chpvUqpRHUgTfhukPl0lh1jAQ=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/cTg51NsMDmEDpzB9Ry7WXVIcvv7DL4HfX7jNEwQWLw=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/f3XzWulYVD1jBA4Jdc2ijUmS5gYkbF7UkYWgnQIuyg=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/PSfxsUaFuMIz5Wy0nTxx9Pd4x_iKg3H8vGedAkX5nQ=w338-h211-p-no");

        imagePaths.add("https://lh3.googleusercontent.com/KdEs91D8cKVDFJTvIVGsmGBkQtD22RN3C9j5mpuHkQ=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/wHHUz57LQmfY_9OHx3ZrmlpkGEiljgzz6ndvfCcSmg=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/3Vlv2PjfRCjOGUNgAMiG8sTytlhItwjT8oMdOzQGKQ=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/zT1mYrIdKkZYSbGJ487VfLP1Vvby58ly_cGUnA9GPQ=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/AdsjDJixWEv9DB5fCngvIdIOadaMOIDQnIR6VZOV9w=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/0QJk24DV24VUgFh7VFqXpL7BfrEYtPftB3vJsnOjIQ=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/8qVW4Yn8u2oI4be2Tm62SBTBRkzJdylI_l5Hu_SEAA=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/OGIF3udxdkfLViq5NtHHi_nh64Cf6AWpxGlws7LitnQ=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/dQNsJuqjC2l27Mc2Ng-v4bdPt6T2VRxyKg_fiGLatg=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/irphqEwXwnH0XXhqe8rtTiKv1DQRIiMqGXqHowW-kA=w338-h211-p-no");

        imagePaths.add("https://lh3.googleusercontent.com/sSCNpqf6tw10T6LL8RoeBbTHJiweqLm6x3mv18hQdA=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/q_7LwrNBzmTiwJr77Zj_Uz3TsCO0aSGCHw3mSZ2ycQ=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/yhDD2UHoWYfHAcJBtTzExTCZmTI1Kwn7TPYjz7PWpQ=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/_FnON-Eq1QoKFr9yJwGHhCckjKuiCMBr5c1F8yIhPw=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/cuo8PWMGAXsRJv0lwxITgBnJuGHMKj2IgDQHCMb73Q=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/Iu5xUed_w7ZScwnE7TbLnJN2JxwGex6433qnZ7jzRQ=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/4gXhuW2H95K53n78egdvyqNRVVXTcaJ3_8OLPPdGhQ=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/QDGX8fwASMZeBqLjvJNs-4XTDRFtBMYsXtdtMTitaQ=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/Ydud69a9zi_9VvGQvpLbKm5aO5yTWYdh3ZfE2wZfLw=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/GBwNpXBooOHlJ746OhRTzens1VBuIEKHtLEeiS_Nog=w338-h211-p-no");

        imagePaths.add("https://lh6.googleusercontent.com/NtGuE8aog--_mvJAdF6KV6jQGgQlIXGbvcCzDqMHGQ=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/9nTJn8Cq1pS1zKGheJrCdlZuNBaozEkHGsn0aPEAmw=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/mivwT-G5v7MUBeaKYhGtqst7cph5E2CWDgoSZ8bJgQ=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/sWus5KniVmfr89Gx7tbT1m46ABnBBRoIzBDEctLkTA=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/cBGXnH8bc6diKbdy7Rsng82tJFcIaM4bbLymSqLM-A=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/Jw7Hv_l3QNwfUIi-1Gpi0xq2ZGCPjPr1nyXQYFPB4w=w338-h211-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/g54LIav50BYBZEx3KAOf5Q1Ye-3rnoDpWQwq9crzbQ=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/vqBLM06iro_PwXoVqENQfy_xFsRwA9oikwB1oepR6g=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/T2bQCffDUt1ci6Q6iCxxEPt5itTSlMjK_kDMZiFFlA=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/sA1wOqO2HLLwkq4n9n-CjqSOTPK0hj4kWYnxF-nMfg=w338-h211-p-no");

        imagePaths.add("https://lh5.googleusercontent.com/XwrZEHixnm1MgcebBgLqA8gfO6DgOGOST5ftzgRzIA=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/PxUOPiMT7ZBb1R2rvbBLesE1quNK6hRF9Xg6UMMe3w=w338-h211-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/6LFynDMHmY4NrjHdGQJAXLh3FYX47sUtwJCii0iMYw=w331-h207-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/l66cPQ_1iYbZgczHghzqmsLA-yrdITQ3zh0Xp1BlyA=w331-h207-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/bLAb0fXqw9r5xq_TIZBX38HqF2txCn-wjhM8-8dKag=w331-h207-p-no");

        return imagePaths;
    }
}
