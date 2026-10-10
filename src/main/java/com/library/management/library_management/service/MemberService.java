package com.library.management.library_management.service;
import com.library.management.library_management.model.Member;
import com.library.management.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) { this.memberRepository = memberRepository; }

    //create a member
    public Member createMember(Member member){
        return memberRepository.save(member);

    }

    //get all members
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    //get one member by id
    public Optional<Member> getMemberById(Integer id){
        return memberRepository.findById(id);

    }
    //update member
    public Member updateMember(Integer id, Member memberDetails){

        return memberRepository.findById(id)
                .map(member ->{
                    if(memberDetails.getName() != null){
                        member.setName(memberDetails.getName());
                    }
                    return memberRepository.save(member);
                }).orElse(null);
    }

    //delete a member
    public void deleteMember(Integer id) {
        memberRepository.deleteById(id);
    }



}
