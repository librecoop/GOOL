#ifndef __FINALLY_H__
#define __FINALLY_H__
class finally_class
{
	private: void (*func)();
	public:
	finally_class(void (*f)())
	{
		func = f;
	}
	~finally_class()
	{
		func();
	}
};

#define finally(args...) finally_class ([&]{args});

#endif
