## English|[中文](https://github.com/chyhongye/IOSDialog/blob/master/README.md)
# A global and local dialog for text, prompts, item, grid, high imitation ios
The dialog box's global dialog box is to solve the problem that the native dialog does not support display on some mobile phones. The local dialog box is written by the way. The global dialog box can be displayed in the launcher even if the app enters the background.
## Global Dialog:
As the name implies, it can be popped directly at any time. It is more suitable for some forced pop-up functions, such as: upgrade dialog, notification dialog, etc.
## Local dialog:
As the name implies, it is a dialog box that depends on an activity or fragment. Once it leaves these contexts, it will not work properly.
# Screenshot
# Text_Dialog
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Text_Dialog.png" width="360" height="640" alt="Loading lose"> )
# Regular_Right_Dialog
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Regular_Right_Dialog.png" width="360" height="640" alt="Loading lose">)
# Item_Dialog
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Item_Dialog.png" width="360" height="640" alt="Loading lose">)
# Grid_Dialog
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Grid_Dialog.png" width="360" height="640" alt="Loading lose">)
# Global Delayed start
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Global%20Delayed%20start1.png" width="360" height="640" alt="Loading lose">)
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Global%20Delayed%20start2.png" width="360" height="640" alt="Loading lose">)
# Local Delayed start.png
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Local%20Delayed%20start1.png" width="360" height="640" alt="Loading lose">)
(<img src="https://github.com/chyhongye/IOSDialog/blob/master/png/Local%20Delayed%20start2.png" width="360" height="640" alt="Loading lose">)
# Complete tutorial
    public class MainActivity extends AppCompatActivity {
        private ActivityMainBinding mainBinding;
        private String[] strs;
        private ArrayList<ContentBean> list = new ArrayList<>();
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            init();
        }
    
        private void init() {
            strs = new String[9];
            for (int i = 0; i < 9; i++) {
                strs[i] = "菜单" + i;
            }
            for (int i = 0; i < 20; i++) {
                list.add(new ContentBean("功能" + i, R.mipmap.ic_launcher_round));
            }
        }
    
        public void onClick(View view) {
            switch (view.getId()) {
                //Global dialog
                case R.id.btn_text_dialog1:
                    GlobalTextDialog globalTextDialog = GlobalTextDialog.getInstance(new CHYOnRightClickListener() {
                        @Override
                        public void onRightClick(View view) {
                            Toast.makeText(MainActivity.this, "点击了全局对话框的rightButton", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Original setting
                    startActivity(globalTextDialog.show(this, new ContentBean("我是内容区", "好的")));
                    //Change text color and set content
                    //startActivity(globalTextDialog.show(this, new ContentBean("我是内容区", "好的"), new ColorBean(Color.RED, Color.BLUE)));
                    //Change the text size and set the content
                    //startActivity(globalTextDialog.show(this, new ContentBean("我是内容区", "好的"), new SizeBean(60f, 90f)));
                    //Change the text size and color and set the content at the same time
                    //startActivity(globalTextDialog.show(this, new ContentBean("我是内容区", "好的"), new ColorBean(Color.RED, Color.BLUE), new SizeBean(60f, 90f)));
                    break;
                case R.id.btn_right_dialog1:
                    startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.RIGHT_DIALOG);
                    break;
                case R.id.btn_error_dialog1:
                    startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.ERROR_DIALOG);
                    break;
                case R.id.btn_warning_dialog1:
                    startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.WARNING_DIALOG);
                    break;
                case R.id.btn_information_dialog1:
                    startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.INFORMATION_DIALOG);
                    break;
                case R.id.btn_item_dialog1:
                    GlobalItemDialog globalItemDialog = GlobalItemDialog.getInstance(null, new CHYOnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Original setting
                    startActivity(globalItemDialog.show(this, strs));
                    //Change text color and set content
                    //startActivity(globalItemDialog.show(this, strs,new ColorBean()));
                    //Change the text size and set the content
                    //startActivity(globalItemDialog.show(this, strs,new SizeBean(30f,0f)));
                    //Change the text size and color and set the content at the same time
                    //startActivity(globalItemDialog.show(this, strs,new ColorBean(),new SizeBean(30f,30f)));
                    break;
                case R.id.btn_grid_dialog1:
                    GlobalGridDialog globalGridDialog = GlobalGridDialog.getInstance(new CHYOnGridClickListener() {
                        @Override
                        public void onGridClick(View view, String title) {
                            Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Original setting
                    startActivity(globalGridDialog.show(this, list));
                    //Change text color and set content
                    //startActivity(globalGridDialog.show(this, list,Color.GREEN));
                    //Change the text size and set the content
                    //startActivity(globalGridDialog.show(this, list,30f));
                    //Change the text size and color and set the content at the same time
                    //startActivity(globalGridDialog.show(this, list,Color.GREEN,30f));
                    break;
                case R.id.btn_delayed_start1:
                    moveTaskToBack(true);
                    Toast.makeText(MainActivity.this, "程序已进入后台状态，4s后将启动一个全局对话框", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlobalTextDialog globalTextDialog = GlobalTextDialog.getInstance(new CHYOnRightClickListener() {
                                @Override
                                public void onRightClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
    
                                }
                            });
                            startActivity(globalTextDialog.show(MainActivity.this, new ContentBean("点击\"启动\"按钮即可启动App", "启动")));
                        }
                    }, 4000);
                    break;
                //Local dialog
                case R.id.btn_text_dialog2:
                    final LocalTextDialog localTextDialog = new LocalTextDialog(this);
                    localTextDialog.createDialog(new ContentBean("我是内容", "好的"), new CHYOnRightClickListener() {
                        @Override
                        public void onRightClick(View view) {
                            Toast.makeText(MainActivity.this, "点击了局部对话框的rightButton", Toast.LENGTH_SHORT).show();
                            localTextDialog.dismiss();
                        }
                    });
                    //Set font color
                    //localTextDialog.setTextColor(new ColorBean(Color.RED, Color.BLUE, Color.GREEN, Color.RED));
                    //Set the font size
                    //localTextDialog.setTextSize(new SizeBean(80f, 50f, 60f, 60f));
                    //Set background
                    localTextDialog.setBackgroundResource(R.mipmap.ic_dialog, false);
                    break;
                case R.id.btn_right_dialog2:
                    startLocalDialog(LocalRegularDialog.DIALOG_TYPE.RIGHT_DIALOG);
                    break;
                case R.id.btn_error_dialog2:
                    startLocalDialog(LocalRegularDialog.DIALOG_TYPE.ERROR_DIALOG);
                    break;
                case R.id.btn_warning_dialog2:
                    startLocalDialog(LocalRegularDialog.DIALOG_TYPE.WARNING_DIALOG);
                    break;
                case R.id.btn_information_dialog2:
                    startLocalDialog(LocalRegularDialog.DIALOG_TYPE.INFORMATION_DIALOG);
                    break;
                case R.id.btn_item_dialog2:
                    LocalItemDialog localItemDialog = new LocalItemDialog(this);
                    localItemDialog.createDialog(strs, "取消", new CHYOnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //localItemDialog.setCancelButtonColor(Color.RED);
                    //localItemDialog.setCancelButtonSize(50f);
                    //Set item font color
                    //localItemDialog.setContentColor(Color.GREEN);
                    //Set the item font size
                    //localItemDialog.setContentSize(30f);
                    break;
                case R.id.btn_grid_dialog2:
                    //Startup mode 1
                    LocalGridDialog fragment = LocalGridDialog.getInstance(list, new CHYOnGridClickListener() {
                        @Override
                        public void onGridClick(View view, String title) {
                            Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                        }
                    });
    
                    //Startup mode 2
                    //Bundle bundle = new Bundle();
                    //bundle.putFloat("size", 30f);
                    //bundle.putInt("color", Color.GREEN);
                    //bundle.putSerializable("content", list);
                    //LocalGridDialog fragment = LocalGridDialog.getInstance(bundle, new CHYOnGridClickListener() {
                    //    @Override
                    //    public void onGridClick(View view, String title) {
                    //        Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                    //   }
                    //});
    
                    //Startup mode 3
                    //LocalGridDialog fragment = LocalGridDialog.getInstance(Color.BLUE, 30f, list, new CHYOnGridClickListener() {
                    //    @Override
                    //    public void onGridClick(View view, String title) {
                    //        Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                    //    }
                    //});
                    fragment.show(getSupportFragmentManager(), null);
                    break;
                case R.id.btn_delayed_start2:
                    moveTaskToBack(true);
                    Toast.makeText(MainActivity.this, "程序已进入后台状态，4s后将启动一个局部对话框", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "对话框已启动，请到程序内部查看", Toast.LENGTH_LONG).show();
                            final LocalTextDialog localTextDialog = new LocalTextDialog(MainActivity.this);
                            localTextDialog.createDialog(new ContentBean("我是内容", "好的"), new CHYOnRightClickListener() {
                                @Override
                                public void onRightClick(View view) {
                                    Toast.makeText(MainActivity.this, "点击了局部对话框的rightButton", Toast.LENGTH_SHORT).show();
                                    localTextDialog.dismiss();
                                }
                            });
                        }
                    }, 4000);
                    break;
            }
        }
    
    
        /**
         * Start global dialog
         *
         * @param dialogType 对话框类型
         */
        private void startGlobalDialog(final GlobalRegularDialog.DIALOG_TYPE dialogType) {
            GlobalRegularDialog globalRegularDialog = GlobalRegularDialog.getInstance(dialogType, new CHYOnRightClickListener() {
                @Override
                public void onRightClick(View view) {
                    Toast.makeText(MainActivity.this, "点击了全局对话框的" + dialogType + "的rightButton", Toast.LENGTH_SHORT).show();
                }
            }, new CHYOnCancelClickListener() {
                @Override
                public void onCancelClick(View view) {
                    Toast.makeText(MainActivity.this, "点击了全局对话框的" + dialogType + "的cancelButton", Toast.LENGTH_SHORT).show();
                }
            });
            //Original setting
            //startActivity(globalRegularDialog.show(this, new ContentBean("我是内容区", "好的")));
            startActivity(globalRegularDialog.show(this, new ContentBean("我是标题", "我是内容区", "取消", "好的")));
            //Change text color and set content
            //startActivity(globalRegularDialog.show(this, new ContentBean("我是标题", "我是内容区", "取消", "好的"), new ColorBean(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY)));
            //Change the text size and set the content
            //startActivity(globalRegularDialog.show(this, new ContentBean("我是标题", "我是内容区", "取消", "好的"), new SizeBean(80f ,50f, 60f ,60f)));
            //Change the text size and color and set the content at the same time
            //startActivity(globalRegularDialog.show(this, new ContentBean("我是标题", "我是内容区", "取消", "好的"), new ColorBean(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY), new SizeBean(80f ,50f, 60f ,60f)));
    
        }
    
        /**
         * Start local dialog
         *
         * @param type 对话框类型
         */
        private void startLocalDialog(final LocalRegularDialog.DIALOG_TYPE type) {
            final LocalRegularDialog localRegularDialog = new LocalRegularDialog(this);
            //ContentBean contentBean = new ContentBean( "我是内容",  "确定");
            ContentBean contentBean = new ContentBean("标题", "我是内容", "取消", "确定");
            localRegularDialog.createDialog(type, contentBean, new CHYOnCancelClickListener() {
                @Override
                public void onCancelClick(View view) {
                    Toast.makeText(MainActivity.this, "点击了局部对话框的" + type + "的cancelButton", Toast.LENGTH_SHORT).show();
                }
            }, new CHYOnRightClickListener() {
                @Override
                public void onRightClick(View view) {
                    Toast.makeText(MainActivity.this, "点击了局部对话框的" + type + "的rightButton", Toast.LENGTH_SHORT).show();
                    localRegularDialog.dismiss();
                }
            });
            //Set font color
            //localRegularDialog.setTextColor(new ColorBean(Color.RED, Color.BLUE, Color.GREEN, Color.RED));
            //Set the font size
            //localRegularDialog.setTextSize(new SizeBean(80f, 50f, 60f, 60f));
            //Set background
            //localRegularDialog.setBackgroundResource(R.mipmap.ic_dialog, false);
        }
    }
