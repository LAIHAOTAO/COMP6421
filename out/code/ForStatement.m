	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 8	% set the stack pointer to the top position of the stack
	addi	R14, R0, 65	% 
	sw	-8(R2), R14	% 
	addi	R8, R0, 0	% for loop initialization
	sw	-4(R2), R8	% 
loop_top_1	lw	R6, -4(R2)	% 
	clti	R6, R6, 3	% 
	bz	R6, loop_end_1	% break out of loop
	lw	R9, -8(R2)	% 
	addi	R9, R9, 1	% 
	sw	-8(R2), R9	% 
	lw	R11, -4(R2)	% 
	addi	R11, R11, 1	% 
	sw	-4(R2), R11	% 
	j	loop_top_1	% 
loop_end_1	nop	% 
	lw	R10, -8(R2)	% 
	putc	R10	% 
	hlt	% ======end of program======
