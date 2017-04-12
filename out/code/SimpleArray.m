	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 24	% set the stack pointer to the top position of the stack
	addi	R14, R0, 64	% 
	sw	-20(R2), R14	% 
	addi	R8, R0, 1	% 
	sw	-16(R2), R8	% 
	lw	R6, -20(R2)	% 
	lw	R9, -16(R2)	% 
	add	R6, R6, R9	% 
	putc	R6	% 
	hlt	% ======end of program======
