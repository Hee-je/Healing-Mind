package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Je_Servey_Activity extends AppCompatActivity {

    private static final String TAG = "phpquerytest";

    String ID, IP, NAME, BIRTH, SEX, PHONE, EMAIL, FIRST, THEMA, RECENTLY, mJsonString;


    RadioButton a1, a2, a3, a4;
    RadioButton b1, b2, b3, b4;
    RadioButton c1, c2, c3, c4;
    RadioButton d1, d2, d3, d4;
    RadioButton e1, e2, e3, e4;
    RadioButton f1, f2, f3, f4;
    RadioButton g1, g2, g3, g4;
    RadioButton h1, h2, h3, h4;
    RadioButton i1, i2, i3, i4;
    RadioButton j1, j2, j3, j4;
    RadioButton k1, k2, k3, k4;
    RadioButton l1, l2, l3, l4;
    RadioButton m1, m2, m3, m4;
    RadioButton n1, n2, n3, n4;
    RadioButton o1, o2, o3, o4;
    RadioButton p1, p2, p3, p4;
    RadioButton q1, q2, q3, q4;
    RadioButton r1, r2, r3, r4;
    RadioButton s1, s2, s3, s4;
    RadioButton t1, t2, t3, t4;
    RadioButton u1, u2, u3, u4;
    RadioButton v1, v2, v3, v4;
    RadioButton w1, w2, w3, w4;
    RadioButton x1, x2, x3, x4;
    Button next;
    boolean aa1, aa2, aa3, aa4, bb1, bb2, bb3, bb4, cc1, cc2, cc3, cc4, dd1, dd2, dd3, dd4, ee1,
            ee2, ee3, ee4, ff1, ff2, ff3, ff4, gg1, gg2, gg3, gg4, hh1, hh2, hh3, hh4, ii1, ii2,
            ii3, ii4, jj1, jj2, jj3, jj4, kk1, kk2, kk3, kk4, ll1, ll2, ll3, ll4, mm1, mm2, mm3,
            mm4, nn1, nn2, nn3, nn4, oo1, oo2, oo3, oo4, pp1, pp2, pp3, pp4, qq1, qq2, qq3, qq4,
            rr1, rr2, rr3, rr4, ss1, ss2, ss3, ss4, tt1, tt2, tt3, tt4, uu1, uu2, uu3, uu4, vv1,
            vv2, vv3, vv4, ww1, ww2, ww3, ww4, xx1, xx2, xx3, xx4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_servey_activity);

        Intent get_intent = getIntent();
        IP = get_intent.getStringExtra("IP");
        ID = get_intent.getStringExtra("ID");
        NAME = get_intent.getStringExtra("NAME");
        BIRTH = get_intent.getStringExtra("BIRTH");
        SEX = get_intent.getStringExtra("SEX");
        PHONE = get_intent.getStringExtra("PHONE");
        EMAIL = get_intent.getStringExtra("EMAIL");
        FIRST = get_intent.getStringExtra("FIRST");
        THEMA = get_intent.getStringExtra("THEMA");
        RECENTLY = get_intent.getStringExtra("RECENTLY");


        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);
        d4 = findViewById(R.id.d4);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);
        f1 = findViewById(R.id.f1);
        f2 = findViewById(R.id.f2);
        f3 = findViewById(R.id.f3);
        f4 = findViewById(R.id.f4);
        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);
        g4 = findViewById(R.id.g4);
        h1 = findViewById(R.id.h1);
        h2 = findViewById(R.id.h2);
        h3 = findViewById(R.id.h3);
        h4 = findViewById(R.id.h4);
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);
        i4 = findViewById(R.id.i4);
        j1 = findViewById(R.id.j1);
        j2 = findViewById(R.id.j2);
        j3 = findViewById(R.id.j3);
        j4 = findViewById(R.id.j4);
        k1 = findViewById(R.id.k1);
        k2 = findViewById(R.id.k2);
        k3 = findViewById(R.id.k3);
        k4 = findViewById(R.id.k4);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        m1 = findViewById(R.id.m1);
        m2 = findViewById(R.id.m2);
        m3 = findViewById(R.id.m3);
        m4 = findViewById(R.id.m4);
        n1 = findViewById(R.id.n1);
        n2 = findViewById(R.id.n2);
        n3 = findViewById(R.id.n3);
        n4 = findViewById(R.id.n4);
        o1 = findViewById(R.id.o1);
        o2 = findViewById(R.id.o2);
        o3 = findViewById(R.id.o3);
        o4 = findViewById(R.id.o4);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        w3 = findViewById(R.id.w3);
        w4 = findViewById(R.id.w4);
        x1 = findViewById(R.id.x1);
        x2 = findViewById(R.id.x2);
        x3 = findViewById(R.id.x3);
        x4 = findViewById(R.id.x4);
        next = findViewById(R.id.next);

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a2.setChecked(false);
                a3.setChecked(false);
                a4.setChecked(false);
                aa1 = true;
                aa2 = false;
                aa3 = false;
                aa4 = false;
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setChecked(false);
                a3.setChecked(false);
                a4.setChecked(false);
                aa1 = false;
                aa2 = true;
                aa3 = false;
                aa4 = false;
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setChecked(false);
                a2.setChecked(false);
                a4.setChecked(false);
                aa1 = false;
                aa2 = false;
                aa3 = true;
                aa4 = false;
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setChecked(false);
                a2.setChecked(false);
                a3.setChecked(false);
                aa1 = false;
                aa2 = false;
                aa3 = false;
                aa4 = true;
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setChecked(false);
                b3.setChecked(false);
                b4.setChecked(false);
                bb1 = true;
                bb2 = false;
                bb3 = false;
                bb4 = false;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setChecked(false);
                b3.setChecked(false);
                b4.setChecked(false);
                bb1 = false;
                bb2 = true;
                bb3 = false;
                bb4 = false;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setChecked(false);
                b2.setChecked(false);
                b4.setChecked(false);
                bb1 = false;
                bb2 = false;
                bb3 = true;
                bb4 = false;
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setChecked(false);
                b2.setChecked(false);
                b3.setChecked(false);
                bb1 = false;
                bb2 = false;
                bb3 = false;
                bb4 = true;
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c2.setChecked(false);
                c3.setChecked(false);
                c4.setChecked(false);
                cc1 = true;
                cc2 = false;
                cc3 = false;
                cc4 = false;
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.setChecked(false);
                c3.setChecked(false);
                c4.setChecked(false);
                cc1 = false;
                cc2 = true;
                cc3 = false;
                cc4 = false;
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.setChecked(false);
                c2.setChecked(false);
                c4.setChecked(false);
                cc1 = false;
                cc2 = false;
                cc3 = true;
                cc4 = false;
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.setChecked(false);
                c2.setChecked(false);
                c3.setChecked(false);
                cc1 = false;
                cc2 = false;
                cc3 = false;
                cc4 = true;
            }
        });

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2.setChecked(false);
                d3.setChecked(false);
                d4.setChecked(false);
                dd1 = true;
                dd2 = false;
                dd3 = false;
                dd4 = false;
            }
        });
        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d1.setChecked(false);
                d3.setChecked(false);
                d4.setChecked(false);
                dd1 = false;
                dd2 = true;
                dd3 = false;
                dd4 = false;
            }
        });
        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d1.setChecked(false);
                d2.setChecked(false);
                d4.setChecked(false);
                dd1 = false;
                dd2 = false;
                dd3 = true;
                dd4 = false;
            }
        });
        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d1.setChecked(false);
                d2.setChecked(false);
                d3.setChecked(false);
                dd1 = false;
                dd2 = false;
                dd3 = false;
                dd4 = true;
            }
        });

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setChecked(false);
                e3.setChecked(false);
                e4.setChecked(false);
                ee1 = true;
                ee2 = false;
                ee3 = false;
                ee4 = false;
            }
        });
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setChecked(false);
                e3.setChecked(false);
                e4.setChecked(false);
                ee1 = false;
                ee2 = true;
                ee3 = false;
                ee4 = false;
            }
        });
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setChecked(false);
                e2.setChecked(false);
                e4.setChecked(false);
                ee1 = false;
                ee2 = false;
                ee3 = true;
                ee4 = false;
            }
        });
        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setChecked(false);
                e2.setChecked(false);
                e3.setChecked(false);
                ee1 = false;
                ee2 = false;
                ee3 = false;
                ee4 = true;
            }
        });

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                ff1 = true;
                ff2 = false;
                ff3 = false;
                ff4 = false;
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                ff1 = false;
                ff2 = true;
                ff3 = false;
                ff4 = false;
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1.setChecked(false);
                f2.setChecked(false);
                f4.setChecked(false);
                ff1 = false;
                ff2 = false;
                ff3 = true;
                ff4 = false;
            }
        });
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                ff1 = false;
                ff2 = false;
                ff3 = false;
                ff4 = true;
            }
        });

        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g2.setChecked(false);
                g3.setChecked(false);
                g4.setChecked(false);
                gg1 = true;
                gg2 = false;
                gg3 = false;
                gg4 = false;
            }
        });
        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g1.setChecked(false);
                g3.setChecked(false);
                g4.setChecked(false);
                gg1 = false;
                gg2 = true;
                gg3 = false;
                gg4 = false;
            }
        });
        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g1.setChecked(false);
                g2.setChecked(false);
                g4.setChecked(false);
                gg1 = false;
                gg2 = false;
                gg3 = true;
                gg4 = false;
            }
        });
        g4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g1.setChecked(false);
                g2.setChecked(false);
                g3.setChecked(false);
                gg1 = false;
                gg2 = false;
                gg3 = false;
                gg4 = true;
            }
        });

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h2.setChecked(false);
                h3.setChecked(false);
                h4.setChecked(false);
                hh1 = true;
                hh2 = false;
                hh3 = false;
                hh4 = false;
            }
        });
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1.setChecked(false);
                h3.setChecked(false);
                h4.setChecked(false);
                hh1 = false;
                hh2 = true;
                hh3 = false;
                hh4 = false;
            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1.setChecked(false);
                h2.setChecked(false);
                h4.setChecked(false);
                hh1 = false;
                hh2 = false;
                hh3 = true;
                hh4 = false;
            }
        });
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1.setChecked(false);
                h2.setChecked(false);
                h3.setChecked(false);
                hh1 = false;
                hh2 = false;
                hh3 = false;
                hh4 = true;
            }
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i2.setChecked(false);
                i3.setChecked(false);
                i4.setChecked(false);
                ii1 = true;
                ii2 = false;
                ii3 = false;
                ii4 = false;
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i1.setChecked(false);
                i3.setChecked(false);
                i4.setChecked(false);
                ii1 = false;
                ii2 = true;
                ii3 = false;
                ii4 = false;
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i1.setChecked(false);
                i2.setChecked(false);
                i4.setChecked(false);
                ii1 = false;
                ii2 = false;
                ii3 = true;
                ii4 = false;
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i1.setChecked(false);
                i2.setChecked(false);
                i3.setChecked(false);
                ii1 = false;
                ii2 = false;
                ii3 = false;
                ii4 = true;
            }
        });

        j1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j2.setChecked(false);
                j3.setChecked(false);
                j4.setChecked(false);
                jj1 = true;
                jj2 = false;
                jj3 = false;
                jj4 = false;
            }
        });
        j2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j1.setChecked(false);
                j3.setChecked(false);
                j4.setChecked(false);
                jj1 = false;
                jj2 = true;
                jj3 = false;
                jj4 = false;
            }
        });
        j3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j1.setChecked(false);
                j2.setChecked(false);
                j4.setChecked(false);
                jj1 = false;
                jj2 = false;
                jj3 = true;
                jj4 = false;
            }
        });
        j4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j1.setChecked(false);
                j2.setChecked(false);
                j3.setChecked(false);
                jj1 = false;
                jj2 = false;
                jj3 = false;
                jj4 = true;
            }
        });

        k1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k2.setChecked(false);
                k3.setChecked(false);
                k4.setChecked(false);
                kk1 = true;
                kk2 = false;
                kk3 = false;
                kk4 = false;
            }
        });
        k2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k1.setChecked(false);
                k3.setChecked(false);
                k4.setChecked(false);
                kk1 = false;
                kk2 = true;
                kk3 = false;
                kk4 = false;
            }
        });
        k3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k1.setChecked(false);
                k2.setChecked(false);
                k4.setChecked(false);
                kk1 = false;
                kk2 = false;
                kk3 = true;
                kk4 = false;
            }
        });
        k4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k1.setChecked(false);
                k2.setChecked(false);
                k3.setChecked(false);
                kk1 = false;
                kk2 = false;
                kk3 = false;
                kk4 = true;
            }
        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setChecked(false);
                l3.setChecked(false);
                l4.setChecked(false);
                ll1 = true;
                ll2 = false;
                ll3 = false;
                ll4 = false;
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setChecked(false);
                l3.setChecked(false);
                l4.setChecked(false);
                ll1 = false;
                ll2 = true;
                ll3 = false;
                ll4 = false;
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setChecked(false);
                l2.setChecked(false);
                l4.setChecked(false);
                ll1 = false;
                ll2 = false;
                ll3 = true;
                ll4 = false;
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setChecked(false);
                l2.setChecked(false);
                l3.setChecked(false);
                ll1 = false;
                ll2 = false;
                ll3 = false;
                ll4 = true;
            }
        });

        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m2.setChecked(false);
                m3.setChecked(false);
                m4.setChecked(false);
                mm1 = true;
                mm2 = false;
                mm3 = false;
                mm4 = false;
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1.setChecked(false);
                m3.setChecked(false);
                m4.setChecked(false);
                mm1 = false;
                mm2 = true;
                mm3 = false;
                mm4 = false;
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1.setChecked(false);
                m2.setChecked(false);
                m4.setChecked(false);
                mm1 = false;
                mm2 = false;
                mm3 = true;
                mm4 = false;
            }
        });
        m4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1.setChecked(false);
                m2.setChecked(false);
                m3.setChecked(false);
                mm1 = false;
                mm2 = false;
                mm3 = false;
                mm4 = true;
            }
        });

        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n2.setChecked(false);
                n3.setChecked(false);
                n4.setChecked(false);
                nn1 = true;
                nn2 = false;
                nn3 = false;
                nn4 = false;
            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n1.setChecked(false);
                n3.setChecked(false);
                n4.setChecked(false);
                nn1 = false;
                nn2 = true;
                nn3 = false;
                nn4 = false;
            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n1.setChecked(false);
                n2.setChecked(false);
                n4.setChecked(false);
                nn1 = false;
                nn2 = false;
                nn3 = true;
                nn4 = false;
            }
        });
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n1.setChecked(false);
                n2.setChecked(false);
                n3.setChecked(false);
                nn1 = false;
                nn2 = false;
                nn3 = false;
                nn4 = true;
            }
        });

        o1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                o2.setChecked(false);
                o3.setChecked(false);
                o4.setChecked(false);
                oo1 = true;
                oo2 = false;
                oo3 = false;
                oo4 = false;
            }
        });
        o2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                o1.setChecked(false);
                o3.setChecked(false);
                o4.setChecked(false);
                oo1 = false;
                oo2 = true;
                oo3 = false;
                oo4 = false;
            }
        });
        o3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                o1.setChecked(false);
                o2.setChecked(false);
                o4.setChecked(false);
                oo1 = false;
                oo2 = false;
                oo3 = true;
                oo4 = false;
            }
        });
        o4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                o1.setChecked(false);
                o2.setChecked(false);
                o3.setChecked(false);
                oo1 = false;
                oo2 = false;
                oo3 = false;
                oo4 = true;
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2.setChecked(false);
                p3.setChecked(false);
                p4.setChecked(false);
                pp1 = true;
                pp2 = false;
                pp3 = false;
                pp4 = false;
            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1.setChecked(false);
                p3.setChecked(false);
                p4.setChecked(false);
                pp1 = false;
                pp2 = true;
                pp3 = false;
                pp4 = false;
            }
        });
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1.setChecked(false);
                p2.setChecked(false);
                p4.setChecked(false);
                pp1 = false;
                pp2 = false;
                pp3 = true;
                pp4 = false;
            }
        });
        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1.setChecked(false);
                p2.setChecked(false);
                p3.setChecked(false);
                pp1 = false;
                pp2 = false;
                pp3 = false;
                pp4 = true;
            }
        });

        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q2.setChecked(false);
                q3.setChecked(false);
                q4.setChecked(false);
                qq1 = true;
                qq2 = false;
                qq3 = false;
                qq4 = false;
            }
        });
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1.setChecked(false);
                q3.setChecked(false);
                q4.setChecked(false);
                qq1 = false;
                qq2 = true;
                qq3 = false;
                qq4 = false;
            }
        });
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1.setChecked(false);
                q2.setChecked(false);
                q4.setChecked(false);
                qq1 = false;
                qq2 = false;
                qq3 = true;
                qq4 = false;
            }
        });
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q1.setChecked(false);
                q2.setChecked(false);
                q3.setChecked(false);
                qq1 = false;
                qq2 = false;
                qq3 = false;
                qq4 = true;
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                rr1 = true;
                rr2 = false;
                rr3 = false;
                rr4 = false;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                rr1 = false;
                rr2 = true;
                rr3 = false;
                rr4 = false;
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r2.setChecked(false);
                r4.setChecked(false);
                rr1 = false;
                rr2 = false;
                rr3 = true;
                rr4 = false;
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r2.setChecked(false);
                r3.setChecked(false);
                rr1 = false;
                rr2 = false;
                rr3 = false;
                rr4 = true;
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s2.setChecked(false);
                s3.setChecked(false);
                s4.setChecked(false);
                ss1 = true;
                ss2 = false;
                ss3 = false;
                ss4 = false;
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setChecked(false);
                s3.setChecked(false);
                s4.setChecked(false);
                ss1 = false;
                ss2 = true;
                ss3 = false;
                ss4 = false;
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setChecked(false);
                s2.setChecked(false);
                s4.setChecked(false);
                ss1 = false;
                ss2 = false;
                ss3 = true;
                ss4 = false;
            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setChecked(false);
                s2.setChecked(false);
                s3.setChecked(false);
                ss1 = false;
                ss2 = false;
                ss3 = false;
                ss4 = true;
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                tt1 = true;
                tt2 = false;
                tt3 = false;
                tt4 = false;
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                tt1 = false;
                tt2 = true;
                tt3 = false;
                tt4 = false;
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setChecked(false);
                t2.setChecked(false);
                t4.setChecked(false);
                tt1 = false;
                tt2 = false;
                tt3 = true;
                tt4 = false;
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setChecked(false);
                t2.setChecked(false);
                t3.setChecked(false);
                tt1 = false;
                tt2 = false;
                tt3 = false;
                tt4 = true;
            }
        });

        u1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u2.setChecked(false);
                u3.setChecked(false);
                u4.setChecked(false);
                uu1 = true;
                uu2 = false;
                uu3 = false;
                uu4 = false;
            }
        });
        u2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1.setChecked(false);
                u3.setChecked(false);
                u4.setChecked(false);
                uu1 = false;
                uu2 = true;
                uu3 = false;
                uu4 = false;
            }
        });
        u3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1.setChecked(false);
                u2.setChecked(false);
                u4.setChecked(false);
                uu1 = false;
                uu2 = false;
                uu3 = true;
                uu4 = false;
            }
        });
        u4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u1.setChecked(false);
                u2.setChecked(false);
                u3.setChecked(false);
                uu1 = false;
                uu2 = false;
                uu3 = false;
                uu4 = true;
            }
        });

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v2.setChecked(false);
                v3.setChecked(false);
                v4.setChecked(false);
                vv1 = true;
                vv2 = false;
                vv3 = false;
                vv4 = false;
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setChecked(false);
                v3.setChecked(false);
                v4.setChecked(false);
                vv1 = false;
                vv2 = true;
                vv3 = false;
                vv4 = false;
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setChecked(false);
                v2.setChecked(false);
                v4.setChecked(false);
                vv1 = false;
                vv2 = false;
                vv3 = true;
                vv4 = false;
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setChecked(false);
                v2.setChecked(false);
                v3.setChecked(false);
                vv1 = false;
                vv2 = false;
                vv3 = false;
                vv4 = true;
            }
        });

        w1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w2.setChecked(false);
                w3.setChecked(false);
                w4.setChecked(false);
                ww1 = true;
                ww2 = false;
                ww3 = false;
                ww4 = false;
            }
        });
        w2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w1.setChecked(false);
                w3.setChecked(false);
                w4.setChecked(false);
                ww1 = false;
                ww2 = true;
                ww3 = false;
                ww4 = false;
            }
        });
        w3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w1.setChecked(false);
                w2.setChecked(false);
                w4.setChecked(false);
                ww1 = false;
                ww2 = false;
                ww3 = true;
                ww4 = false;
            }
        });
        w4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w1.setChecked(false);
                w2.setChecked(false);
                w3.setChecked(false);
                ww1 = false;
                ww2 = false;
                ww3 = false;
                ww4 = true;
            }
        });

        x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x2.setChecked(false);
                x3.setChecked(false);
                x4.setChecked(false);
                xx1 = true;
                xx2 = false;
                xx3 = false;
                xx4 = false;
            }
        });
        x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x1.setChecked(false);
                x3.setChecked(false);
                x4.setChecked(false);
                xx1 = false;
                xx2 = true;
                xx3 = false;
                xx4 = false;
            }
        });
        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x1.setChecked(false);
                x2.setChecked(false);
                x4.setChecked(false);
                xx1 = false;
                xx2 = false;
                xx3 = true;
                xx4 = false;
            }
        });
        x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x1.setChecked(false);
                x2.setChecked(false);
                x3.setChecked(false);
                xx1 = false;
                xx2 = false;
                xx3 = false;
                xx4 = true;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            int D = 0, I = 0, S = 0, C = 0; // D 주도형, I 사교형, S 안정형, C 신중형

            @Override
            public void onClick(View v) {

                if ((aa1 == true || aa2 == true || aa3 == true || aa4 == true) &&
                        (bb1 == true || bb2 == true || bb3 == true || bb4 == true) &&
                        (cc1 == true || cc2 == true || cc3 == true || cc4 == true) &&
                        (dd1 == true || dd2 == true || dd3 == true || dd4 == true) &&
                        (ee1 == true || ee2 == true || ee3 == true || ee4 == true) &&
                        (ff1 == true || ff2 == true || ff3 == true || ff4 == true) &&
                        (gg1 == true || gg2 == true || gg3 == true || gg4 == true) &&
                        (hh1 == true || hh2 == true || hh3 == true || hh4 == true) &&
                        (ii1 == true || ii2 == true || ii3 == true || ii4 == true) &&
                        (jj1 == true || jj2 == true || jj3 == true || jj4 == true) &&
                        (kk1 == true || kk2 == true || kk3 == true || kk4 == true) &&
                        (ll1 == true || ll2 == true || ll3 == true || ll4 == true) &&
                        (mm1 == true || mm2 == true || mm3 == true || mm4 == true) &&
                        (nn1 == true || nn2 == true || nn3 == true || nn4 == true) &&
                        (oo1 == true || oo2 == true || oo3 == true || oo4 == true) &&
                        (pp1 == true || pp2 == true || pp3 == true || pp4 == true) &&
                        (qq1 == true || qq2 == true || qq3 == true || qq4 == true) &&
                        (rr1 == true || rr2 == true || rr3 == true || rr4 == true) &&
                        (ss1 == true || ss2 == true || ss3 == true || ss4 == true) &&
                        (tt1 == true || tt2 == true || tt3 == true || tt4 == true) &&
                        (uu1 == true || uu2 == true || uu3 == true || uu4 == true) &&
                        (vv1 == true || vv2 == true || vv3 == true || vv4 == true) &&
                        (ww1 == true || ww2 == true || ww3 == true || ww4 == true) &&
                        (xx1 == true || xx2 == true || xx3 == true || xx4 == true)
                ) {
                    if (aa2 == true) {
                        D++;
                    } else if (aa4 == true) {
                        I++;
                    } else if (aa1 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (bb1 == true) {
                        D++;
                    } else if (bb3 == true) {
                        I++;
                    } else if (bb4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (cc3 == true) {
                        D++;
                    } else if (cc2 == true) {
                        I++;
                    } else if (cc1 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (dd1 == true) {
                        D++;
                    } else if (dd4 == true) {
                        I++;
                    } else if (dd3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ee4 == true) {
                        D++;
                    } else if (ee2 == true) {
                        I++;
                    } else if (ee3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ff2 == true) {
                        D++;
                    } else if (ff1 == true) {
                        I++;
                    } else if (ff4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (gg3 == true) {
                        D++;
                    } else if (gg4 == true) {
                        I++;
                    } else if (gg2 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (hh2 == true) {
                        D++;
                    } else if (hh1 == true) {
                        I++;
                    } else if (hh4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ii4 == true) {
                        D++;
                    } else if (ii1 == true) {
                        I++;
                    } else if (ii3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (jj3 == true) {
                        D++;
                    } else if (jj2 == true) {
                        I++;
                    } else if (jj4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (kk1 == true) {
                        D++;
                    } else if (kk4 == true) {
                        I++;
                    } else if (kk3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ll4 == true) {
                        D++;
                    } else if (ll3 == true) {
                        I++;
                    } else if (ll1 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (mm2 == true) {
                        D++;
                    } else if (mm1 == true) {
                        I++;
                    } else if (mm4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (nn3 == true) {
                        D++;
                    } else if (nn4 == true) {
                        I++;
                    } else if (nn2 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (oo4 == true) {
                        D++;
                    } else if (oo1 == true) {
                        I++;
                    } else if (oo3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (pp1 == true) {
                        D++;
                    } else if (pp2 == true) {
                        I++;
                    } else if (pp3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (qq2 == true) {
                        D++;
                    } else if (qq3 == true) {
                        I++;
                    } else if (qq4 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (rr3 == true) {
                        D++;
                    } else if (rr1 == true) {
                        I++;
                    } else if (rr2 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ss4 == true) {
                        D++;
                    } else if (ss2 == true) {
                        I++;
                    } else if (ss3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (tt1 == true) {
                        D++;
                    } else if (tt4 == true) {
                        I++;
                    } else if (tt3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (uu1 == true) {
                        D++;
                    } else if (uu2 == true) {
                        I++;
                    } else if (uu3 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (vv4 == true) {
                        D++;
                    } else if (vv3 == true) {
                        I++;
                    } else if (vv2 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (ww4 == true) {
                        D++;
                    } else if (ww2 == true) {
                        I++;
                    } else if (ww1 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (xx4 == true) {
                        D++;
                    } else if (xx3 == true) {
                        I++;
                    } else if (xx1 == true) {
                        S++;
                    } else {
                        C++;
                    }

                    if (D >= I) {
                        if (D >= S) {
                            if (D >= C) {
                                THEMA = "D";
                            }
                        }
                    }

                    if (I >= D) {
                        if (I >= S) {
                            if (I >= C) {
                                THEMA = "I";
                            }
                        }
                    }

                    if (S >= D) {
                        if (S >= I) {
                            if (S >= C) {
                                THEMA = "S";
                            }
                        }
                    }

                    if (C >= D) {
                        if (C >= I) {
                            if (C >= S) {
                                THEMA = "C";
                            }
                        }
                    }
                    try {

                        Je_Servey_Activity.GetData task = new Je_Servey_Activity.GetData();
                        task.execute(ID, THEMA);

                        Intent main = new Intent(getApplicationContext(), In_Frame_Activity.class);

                        main.putExtra("NAME", NAME);
                        main.putExtra("BIRTH", BIRTH);
                        main.putExtra("THEMA", THEMA);

                        main.putExtra("SEX", SEX);
                        main.putExtra("PHONE", PHONE);
                        main.putExtra("EMAIL", EMAIL);
                        main.putExtra("FIRST", FIRST);
                        main.putExtra("ID", ID);
                        startActivity(main);
                        finish();

                    } catch (Exception e) {
                        Log.d(TAG, "UpdateError: Error ", e);
                        finish();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "전부 선택해주세요", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    class GetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);
            if (result != null) {
                mJsonString = result;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String thema = params[1];

            String serverURL = "http://" + IP + "/update_thema.php";
            String postParameters = "&id=" + id + "&thema=" + thema;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "UpdateData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }

    }
}
