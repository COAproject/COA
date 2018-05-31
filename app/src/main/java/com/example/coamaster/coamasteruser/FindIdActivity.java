package com.example.coamaster.coamasteruser;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class FindIdActivity extends AppCompatActivity implements View.OnClickListener, Dialog.OnCancelListener {

    private String sendMessage = null; //이메일 내용
    private Button emailOkButton = null; //이메일 인증번호 확인
    private int randomNum;


    EditText findidEmail;
    Button sendcodeBtn;


    /*Dialog에 관련된 필드*/

    LayoutInflater dialog; //LayoutInflater
    View dialogLayout; //layout을 담을 View
    Dialog emailcodeDialog; //dialog 객체


    /*카운트 다운 타이머에 관련된 필드*/

    TextView timeCounter; //시간을 보여주는 TextView
    EditText emailNumber; //인증 번호를 입력 하는 칸
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 300 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        findidEmail = (EditText) findViewById(R.id.findid_email); //받는 사람 이메일
        sendcodeBtn = (Button) findViewById(R.id.sendcodeButton);
        sendcodeBtn.setOnClickListener(this);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //sendMessage = "인증 코드 : "+randomNum ; //본문 내용

    }

    public void countDownTimer() { //카운트 다운 메소드

        timeCounter = (TextView) dialogLayout.findViewById(R.id.emailTimeCounter);
        //줄어드는 시간을 나타내는 TextView
        emailNumber = (EditText) dialogLayout.findViewById(R.id.emailNumber);
        //사용자 인증 번호 입력창
        emailOkButton = (Button) dialogLayout.findViewById(R.id.emailOkButton);
        //인증하기 버튼


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    timeCounter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    timeCounter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료

                emailcodeDialog.cancel();

            }
        }.start();

        emailOkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sendcodeButton:
                try {
                    Toast.makeText(getApplicationContext(), "이메일 전송중입니다.", Toast.LENGTH_SHORT).show();
                    MailSender mailSender = new MailSender("coamaster12@gmail.com", "admin1234!");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    randomNum = mailSender.getEmailCode();
                    mailSender.sendMail("COA 인증 메일입니다.", "인증 코드 : " + randomNum, findidEmail.getText().toString());
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                } catch (SendFailedException e) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                } catch (MessagingException e) {
                    Toast.makeText(getApplicationContext(), "인터넷 연결 또는 이메일을 확인해주십시오", Toast.LENGTH_SHORT).show();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog = LayoutInflater.from(this);
                dialogLayout = dialog.inflate(R.layout.email_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
                emailcodeDialog = new Dialog(this); //Dialog 객체 생성
                emailcodeDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
                emailcodeDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
                emailcodeDialog.setOnCancelListener(this); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
                emailcodeDialog.show(); //Dialog를 나타내어 준다.
                countDownTimer();
                break;


            case R.id.emailOkButton : //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시

                int user_answer = Integer.parseInt(emailNumber.getText().toString());
                if(user_answer==randomNum){
                    Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                    emailcodeDialog.cancel(); //다이얼로그 닫기
                    Intent findidIntent = new Intent(FindIdActivity.this, IdViewActivity.class);
                    findidIntent.putExtra("user_email",findidEmail.getText().toString());
                    FindIdActivity.this.finish();
                    FindIdActivity.this.startActivity(findidIntent);

                }else{

                    Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        countDownTimer.cancel();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출
}
