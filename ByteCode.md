问题：
一个new对象的语句 Test t = new Test(); ，编译成字节码后会是这个样子

    new jvm.study.Test [1]
    dup
    invokespecial jvm.study.Test() [16]
    astore_1 [t]

回答：
题主的源码中，

    new Test()  

这个表达式的作用是：创建并默认初始化一个Test类型的对象调用Test类的signature为 <init>()V 的构造器表达式的值为一个指向这个新建对家的引用。对应到字节码，我们可以看到：new Test 对应上面的(1)invokespecial Test.<init>()V 对应上面的(2)然而(3)是怎么来的？再看实际的字节码 ：        

                                   // operand stack:
                                   // ...
    new Test                       // ..., ref
    dup                            // ..., ref, ref
    invokespecial Test.<init>()V   // ..., ref

可以看到，new字节码指令的作用是创建指定类型的对象实例、对其进行默认初始化，并且将指向该实例的一个引用压入操作数栈顶；然后因为invokespecial会消耗掉操作数栈顶的引用作为传给构造器的“this”参数，所以如果我们希望在invokespecial调用后在操作数栈顶还维持有一个指向新建对象的引用，就得在invokespecial之前先“复制”一份引用——这就是这个dup的来源。在上面的基础上，我们把这个引用保存到局部变量去，就有：

                                   // operand stack:
                                   // ..., ref
    astore_1                       // ...

astore就会把操作数栈顶的那个引用消耗掉，保存到指定的局部变量去。


二.JVM指令之invokestatic,invokespecial,invokeinterface,invokevirtual,invokedynamic  
1. invokestatic: 用以调用类方法（Invoke a class (static) method ）  
2. invokespecial: 指令用于调用一些需要特殊处理的实例方法，包括实例初始化方法、私有方法和父类方法。（Invoke instance method; special handling for superclass, private, and instance initialization method invocations ）  
3. invokeinterface：用以调用接口方法，在运行时搜索一个实现了这个接口方法的对象，找出适合的方法进行调用。（Invoke interface method）  
4. invokevirtual:指令用于调用对象的实例方法，根据对象的实际类型进行分派（Invoke instance method; dispatch based on class）  
PS：补充个人理解，这里应该是指编译期无法确定调用对象，比如static和private修饰的方法都是编译期就可以确定调用对象。非private方法可被复写（override），编译期无法确定谁调用。
5. invokedynamic: JDK1.7新加入的一个虚拟机指令，相比于之前的四条指令，他们的分派逻辑都是固化在JVM内部，而invokedynamic则用于处理新的方法分派：它允许应用级别的代码来确定执行哪一个方法调用，只有在调用要执行的时候，才会进行这种判断,从而达到动态语言的支持。(Invoke dynamic method)  
