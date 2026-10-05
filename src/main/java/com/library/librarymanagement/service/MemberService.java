package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.MemberRequest;
import com.library.librarymanagement.entity.Member;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        )
                );
    }

    public Member addMember(MemberRequest request) {

        Member member = new Member();

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());

        member.setCreatedAt(LocalDateTime.now());

        return memberRepository.save(member);
    }

    public Member updateMember(Long id, MemberRequest request) {

        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        )
                );

        existingMember.setName(request.getName());
        existingMember.setEmail(request.getEmail());
        existingMember.setPhone(request.getPhone());
        existingMember.setAddress(request.getAddress());

        return memberRepository.save(existingMember);
    }

    public void deleteMember(Long id) {

        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id: " + id
                        )
                );

        memberRepository.delete(existingMember);
    }
}