package com.cberthier.bankaccount.service.security;

import com.cberthier.bankaccount.domain.model.Client;
import com.cberthier.bankaccount.domain.model.ClientDetailsImpl;
import com.cberthier.bankaccount.domain.repository.ClientCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
public class ClientDetailsServiceImpl implements UserDetailsService {

    ClientCrudRepository clientCrudRepository;

    public ClientDetailsServiceImpl(@Autowired ClientCrudRepository clientCrudRepository) {
        this.clientCrudRepository = clientCrudRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("No user found");
        }
        Client client = clientCrudRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return ClientDetailsImpl.build(client);
    }
}
