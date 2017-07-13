/**
 * 
 */
package net.combase.scale;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author "Till Freier"
 *
 */
public class ScaleConnector
{
	private ScanCodeHandler scanCodeHandler;

	private int lastWeight = -1;

	public ScanCodeHandler getScanCodeHandler()
	{
		return scanCodeHandler;
	}

	public void setScanCodeHandler(ScanCodeHandler scanCodeHandler)
	{
		this.scanCodeHandler = scanCodeHandler;
	}

	private void connect() throws IOException
	{
		File f = new File("/dev/ttyS0");

		if (!f.exists())
		{
			System.err.println("file " + f.getAbsolutePath() + " doesn't exist.");
			System.exit(1);
		}

		FileReader fr = new FileReader(f);

		int b = -1;

		StringBuilder buffer = new StringBuilder();
		int readCode = 0;
		int readScaleData = 0;
		boolean scale = false;
		boolean scan = false;
		while ((b = fr.read()) != -1)
		{
			char c = (char)b;


			System.out.println("char: " + c);

			if (readCode > 0)
			{
				buffer.append(c);
				readCode--;
				if (readCode == 0)
				{
					String cmd = buffer.toString();
					System.out.println("read code: " + cmd);
					buffer = new StringBuilder();
					scale = (cmd.startsWith("14"));
					scan = !scale;

					if (scale)
						readScaleData = 4;
				}

				continue;
			}


			if (scale && readScaleData < 1)
			{
				scale = false;
				try
				{
					int weight = Integer.parseInt(buffer.toString());
					weight *= 10;
					synchronized (this)
					{
						lastWeight = weight;
					}
					System.out.println("set last weight: " + weight);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				buffer = new StringBuilder();

				continue;
			}

			if (scale && readScaleData-- > 0)
			{
				buffer.append(c);
				continue;
			}

			if (scan)
			{
				if (c == '\t')
				{
					passCode(buffer.toString());
					buffer = new StringBuilder();
					scan = false;
				}
				else
					buffer.append(c);

				continue;
			}


			if (c == 'S')
			{
				readCode = 3;
			}

		}

		System.err.println("stream closed!!!");
		fr.close();
	}

	public int popWeight()
	{
		int w = -1;

		synchronized (this)
		{
			w = lastWeight;
			lastWeight = -1;
		}

		return w;
	}

	public void refreshWeight()
	{
		System.out.println("refresh weight");
		try
		{
			FileWriter fw = new FileWriter("/dev/ttyS0");
			fw.write(Integer.valueOf("53", 16));
			fw.write(Integer.valueOf("31", 16));
			fw.write(Integer.valueOf("34", 16));
			fw.write(Integer.valueOf("0D", 16));
			//fw.write(Integer.valueOf("0A", 16));
			fw.flush();
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void initRunner()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					connect();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void passCode(String data)
	{
		if (scanCodeHandler != null)
			scanCodeHandler.handle(data);
	}
}
