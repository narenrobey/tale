package com.robey.tale;

import java.io.FileInputStream;
import java.io.IOException;

class Tale {
	private static final int BUFFER_SIZE = 512;
	private static final int SLEEP_DURATION = 500;

	public static void main(final String[] args) {

		TaleContext ctx = TaleContext.getAppContextFromCLIArgs(args);
		if(ctx == null){
			System.exit(1);
			return;
		}

		//read from the file
		byte[] buffer = new byte[BUFFER_SIZE];

		try (FileInputStream fileStream = new FileInputStream(ctx.file)) {
			
			final int bytesToSkip = ctx.bytesToSkip();
			if(bytesToSkip > 0){
				fileStream.skip(bytesToSkip);
			}

			int numRead = fileStream.read(buffer);

			if(!ctx.isForever()){
				while(numRead != -1) {

					System.out.print(new String( buffer, 0, numRead));

					numRead = fileStream.read(buffer);
				}
			}
			else{
				// we're reading forever, so block while numRead is -1
				while(true) {
					if(numRead == -1){
						try {
							Thread.sleep(SLEEP_DURATION);

						} catch (InterruptedException e) {
							// interrupted, so exit
							System.out.println("Interrupted, exiting");
							break;
						}
					}
					else {
						System.out.print(new String( buffer, 0, numRead));
					}
					numRead = fileStream.read(buffer);
				}

				System.out.println(new String( buffer, 0, numRead));
			}
		}
		catch(IOException ex) {
			System.err.println( "Error: " + ex.getMessage());
		}

		System.exit(0);
	}

}
