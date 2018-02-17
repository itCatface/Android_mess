1. 在Application中初始化公/私有云

	MscT.init(getApplicationContext());


2. 调用方式

	// USE_PUBLIC_CLOUD:  使用公有云
	// USE_PRIVATE_CLOUD: 使用私有云
	MscT mMsct = MscT.getInstance(getApplicationContext(), MscT.USE_PUBLIC_CLOUD);
    mMsct.setMscListener(new IMscListener() { ... }