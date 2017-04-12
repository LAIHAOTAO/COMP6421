f11496724653	sw	4(R2), R3	% store return address
	addi	R13, R0, 65	% 
	lw	R7, 8(R2)	% 
	sw	0(R7), R13	% 
	addi	R15, R0, 2	% 
	lw	R5, 8(R2)	% 
	sw	4(R5), R15	% 
	lw	R8, 8(R2)	% 
	lw	R8, 0(R8)	% 
	lw	R10, 8(R2)	% 
	lw	R10, 4(R10)	% 
	add	R8, R8, R10	% 
	sw	-4(R2), R8	% 
	lw	R9, -4(R2)	% 
	add	R3, R0, R9	% return value is a register value, get its value
	lw	R11, 4(R2)	% get return address
	lw	R2, 0(R2)	% load the previous frame pointer address
	addi	R1, R1, 16	% reset the stack pointer
	jr	R11	% 
	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 16	% set the stack pointer to the top position of the stack
	addi	R13, R2, -16	% 
	sw	-4(R1), R13	% add parameter
	sw	-12(R1), R2	% store the previous frame pointer
	addi	R2, R1, -12	% update the frame pointer
	addi	R1, R2, -4	% update the stack pointer
	jl	R3, f11496724653	% store return address and jump to f11496724653
	sw	-4(R2), R3	% 
	lw	R7, -4(R2)	% 
	putc	R7	% 
	hlt	% ======end of program======
