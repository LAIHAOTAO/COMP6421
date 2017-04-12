	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 4	% set the stack pointer to the top position of the stack
	getc	R14	% 
	sw	-4(R2), R14	% 
	lw	R8, -4(R2)	% 
	putc	R8	% 
	hlt	% ======end of program======
