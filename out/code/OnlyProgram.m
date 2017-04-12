	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 12	% set the stack pointer to the top position of the stack
	addi	R14, R0, 2	% 
	sw	-12(R2), R14	% 
	addi	R8, R0, 34	% 
	sw	-8(R2), R8	% 
	lw	R6, -12(R2)	% 
	lw	R9, -8(R2)	% 
	lw	R11, -12(R2)	% 
	mul	R9, R9, R11	% 
	add	R6, R6, R9	% 
	sw	-4(R2), R6	% 
	lw	R10, -4(R2)	% 
	putc	R10	% 
	hlt	% ======end of program======
