package mytweet2;

import java.awt.EventQueue;

import javax.management.modelmbean.ModelMBean;
import javax.swing.JFrame;
import javax.swing.JLabel;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListCellRenderer;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;

public class Main implements DocumentListener {

	private JFrame frame;
	private ConfigurationBuilder cb;
	private TwitterFactory tf;
	private User user;
	private Twitter twitter;
	private JLabel lbcount;
	private DefaultListModel model;
	private JTextPane stuTxt;
	private JScrollPane listScrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws TwitterException
	 */
	public Main() throws TwitterException {
		twInit();
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws TwitterException
	 */
	private void initialize() throws TwitterException {
		frame = new JFrame();
		frame.setTitle("ツイット窓");
		frame.setOpacity(1.0f);
		frame.setBounds(100, 100, 777, 780);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 689, 81, 0 };
		gbl_panel.rowHeights = new int[] { 51, 163, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		stuTxt = new JTextPane();
		stuTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// lbcount.setText(Integer.toString(stuTxt.getText().length()));
			}
		});

		JLabel lblDd = new JLabel("近況を報告しよう");
		GridBagConstraints gbc_lblDd = new GridBagConstraints();
		gbc_lblDd.insets = new Insets(0, 0, 5, 5);
		gbc_lblDd.gridx = 0;
		gbc_lblDd.gridy = 0;
		panel.add(lblDd, gbc_lblDd);
		lbcount = new JLabel("0");
		GridBagConstraints gbc_lbcount = new GridBagConstraints();
		gbc_lbcount.insets = new Insets(0, 0, 5, 0);
		gbc_lbcount.fill = GridBagConstraints.VERTICAL;
		gbc_lbcount.gridx = 1;
		gbc_lbcount.gridy = 0;
		panel.add(lbcount, gbc_lbcount);
		GridBagConstraints gbc_stuTxt = new GridBagConstraints();
		gbc_stuTxt.gridwidth = 2;
		gbc_stuTxt.fill = GridBagConstraints.BOTH;
		gbc_stuTxt.insets = new Insets(0, 0, 5, 0);
		gbc_stuTxt.gridx = 0;
		gbc_stuTxt.gridy = 1;
		panel.add(stuTxt, gbc_stuTxt);

		JButton btn_twit = new JButton("ついっと");
		btn_twit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Status status = twitter.updateStatus(stuTxt.getText());
					JOptionPane.showMessageDialog(null, "ボソッ");
					stuTxt.setText("");
				} catch (TwitterException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					System.err.println(e1);
					switch (e1.getErrorCode()) {
					case 187:
						JOptionPane.showMessageDialog(null, "重複してる");
						break;
					case 170:
						JOptionPane.showMessageDialog(null, "なんか言えや");
						break;
					case 186:
						JOptionPane.showMessageDialog(null, "文字大杉");
						break;
					}

				}
			}
		});
		GridBagConstraints gbc_btn_twit = new GridBagConstraints();
		gbc_btn_twit.insets = new Insets(0, 0, 5, 0);
		gbc_btn_twit.gridwidth = 2;
		gbc_btn_twit.fill = GridBagConstraints.BOTH;
		gbc_btn_twit.gridx = 0;
		gbc_btn_twit.gridy = 2;
		panel.add(btn_twit, gbc_btn_twit);

		model = new DefaultListModel();
		JList list = new JList(model);
		listScrollPane = new JScrollPane(list);
		
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 3;
		
		list.setCellRenderer(new MyListCellRenderer());
		list.setFixedCellWidth(list.getWidth());
		panel.add(listScrollPane, gbc_list);
		
	

		stuTxt.getDocument().addDocumentListener(this);

		// Twitter twitter = TwitterFactory.getSingleton();

		// Runnable ru = new Runnable() {
		/*
		 * public void run() { List<Status> statuses = null;
		 * 
		 * try { statuses = twitter.getHomeTimeline();
		 * System.out.println("Showing home timeline."); for (Status status :
		 * statuses) { System.out.println(status.getUser().getName() + ":" +
		 * status.getText()); model.addElement(status.getUser().getName() + ":"
		 * + status.getText()); } } catch (TwitterException e1) { // TODO
		 * 自動生成された catch ブロック e1.printStackTrace(); } /* while (false) { try {
		 * List<Status> tmpList = twitter.getHomeTimeline(); for (Status s :
		 * tmpList) { if (statuses.contains(s) == false) { statuses.add(0, s); ;
		 * } }
		 * 
		 * System.out.println("Showing home timeline."); for (Status status :
		 * statuses) { System.out.println(status.getUser().getName() + ":" +
		 * status.getText()); model.addElement(status.getUser().getName() + ":"
		 * + status.getText()); } } catch (TwitterException e) { // TODO 自動生成された
		 * catch ブロック e.printStackTrace(); } }
		 * 
		 * 
		 * } };
		 * 
		 * ru.run();
		 */

		List<Status> statuses = null;

		statuses = twitter.getHomeTimeline();
		System.out.println("Showing home timeline.");
		for (Status status : statuses) {
			System.out.println(orig2Str(status));
			model.addElement(orig2Str(status));

		}
	}

	private void twInit() throws TwitterException {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("YO8sD0BEv1wWrafN1m8UQMvJq")
				.setOAuthConsumerSecret(
						"KUyoxDWdCgslSoNEWYLSvwVrnDLuyBgQA5K4IeI180pLab5Rej")
				.setOAuthAccessToken(
						"3248383952-ip0r83rAeWuOo0h0JatymmjrR9sQWJnqArTXaNs")
				.setOAuthAccessTokenSecret(
						"f5JHjTNATSnNNu3DbSh4NgfPjQJlw7RhHCKsvsc4CE2sg");

		Configuration cbd = cb.build();
		tf = new TwitterFactory(cbd);

		twitter = tf.getInstance();

		/*
		 * twitter.setOAuthConsumer("YO8sD0BEv1wWrafN1m8UQMvJq ", //
		 * アプリケーションのconsumer // key
		 * "KUyoxDWdCgslSoNEWYLSvwVrnDLuyBgQA5K4IeI180pLab5Rej"); //
		 * アプリケーションのconsumer // secret
		 * 
		 * AccessToken accessToken = new AccessToken(
		 * "3248383952-ip0r83rAeWuOo0h0JatymmjrR9sQWJnqArTXaNs", // 自分のAccess //
		 * token "f5JHjTNATSnNNu3DbSh4NgfPjQJlw7RhHCKsvsc4CE2sg"); // 自分のAccess
		 * // token // secret
		 * 
		 * 
		 * twitter.setOAuthAccessToken(accessToken);//
		 * 自分のアクセストークンをTwitterオブジェクトに格納
		 */
		/*
		 * TwitterStream twStream = new
		 * TwitterStreamFactory(cb.build()).getInstance();
		 * twStream.addListener(new StreamListener());
		 */

		TwitterStream twitterStream = new TwitterStreamFactory(cbd)
				.getInstance();
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				System.out.println(orig2Str(status));
				model.add(0, orig2Str(status));
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		twitterStream.addListener(listener);
		twitterStream.user();

		user = twitter.verifyCredentials();

		System.out.println(user.getId());// 自分のアカウントのIDの取得（数字のID）
		System.out.println(user.getName());// 自分のアカウントの名前を取得
		System.out.println(user.getScreenName());// 自分のアカウントのUserNameを取得（アルファベットのみの名前）
		System.out.println(user.getLocation());// 自分のアカウントのプロフィールの場所を取得
		System.out.println(user.getDescription());// 自分のアカウントのプロフィールの説明を取得
		System.out.println(user.getProfileImageURL());// 自分のアカウントのプロフィール画像のURLを取得
		System.out.println(user.getURL());// 自分のアカウントのプロフィールのURLを取得する
		System.out.println(user.isProtected());// 自分のアカウントに鍵がついてるか取得する
		System.out.println(user.getFollowersCount());// 自分のアカウントのフォロワー数を取得する
		System.out.println(user.getFriendsCount());// 自分のアカウントのフォロー数を取得する
		System.out.println(user.getCreatedAt());// 自分のアカウントの登録日を取得する
		System.out.println(user.getFavouritesCount());// 自分のアカウントのお気に入り数を取得する
		System.out.println(user.getProfileBannerURL());// 自分のアカウントのバナー画像を取得する
		System.out.println(user.getStatusesCount()); // 呟きの数を取得
		System.out.println(user.getListedCount());// 追加されているリストの数を取得する

		/*
		 * try{ //Status status = twitter.updateStatus("はぁ〜〜〜"); } catch
		 * (TwitterException e) { // TODO: handle exception }
		 */

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		lbcount.setText(Integer.toString(stuTxt.getText().length()));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		lbcount.setText(Integer.toString(stuTxt.getText().length()));

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		lbcount.setText(Integer.toString(stuTxt.getText().length()));
	}

	public String orig2Str(Status status) {
		return status.getUser().getName() + " @"
				+ status.getUser().getScreenName() + " \n " + status.getText();
	}

}

class MyListCellRenderer /*extends JTextArea */implements ListCellRenderer {
	  private static final long serialVersionUID = 1L;
	  
	  @Override
	  public Component getListCellRendererComponent(
	      JList list, Object object,int index, 
	      boolean isSelected, boolean hasFocus) {
	    JTextArea ta = new JTextArea((String)object);
	    ta.setOpaque(true);
	    if (isSelected){
	      ta.setBackground(new Color(255,220,220));
	    } else {
	      ta.setBackground(new Color(220,220,255));
	    }
	    //label.setFont(new Font("Serif",Font.BOLD,16));
	    
	  //  ta.setBounds(0, 0, list.getWidth(), ta.getFont().getSize()*4/72);
	    ta.setLineWrap(true);
	    ta.setWrapStyleWord(false);
	    ta.setForeground(new Color(0,0,100));
	    ta.setBorder(new LineBorder(Color.black, 1));
	    ta.setFocusable(true);
	    ta.setEnabled(true);
	    ta.setEditable(true);
	    return ta;
	  }
}
