package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.ExtractCommandException;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.SignupRepo;
import ru.kets.barsik.repo.pojo.Role;
import ru.kets.barsik.repo.pojo.Signup;

import javax.annotation.Resource;
import java.util.List;

@Component("signup")
public class SignupCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "signup";

    @Resource
    private SignupRepo signupRepo;

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            Signup signup;
            switch (commandPair.getLeft()) {
                // barsik create {name}
                case "create":
                    signup = signupRepo.findSignupByName(commandPair.getRight());
                    if (signup == null) {
                        signup = new Signup();
                        signup.setName(commandPair.getRight());
                    } else {
                        signup.getRoles()
                                .forEach(r -> r.setUser(null));
                    }

                    signupRepo.save(signup);
                    return signup.toString();
                // barsik addRole {name} {role}
                case "addRole":
                    Pair<String, String> namePair = CommandHelper.extractCommand(commandPair.getRight());

                    signup = signupRepo.findSignupByName(namePair.getLeft());
                    if (signup == null) {
                        return ("Signup not found");
                    }
                    Role role = new Role();
                    role.setRole(namePair.getRight());
                    signup.getRoles().add(role);

                    signupRepo.save(signup);

                    return signup.toString();
                // barsik add {role}
                case "add":
                    List<Signup> signups = signupRepo.findSignupsByChannelIdAndActive(eventMessage.getChannel().getId(), true);
                    if(signups.size() < 1) {
                        return "error no active";
                    }
                    if (signups.size() > 1) {
                        return "error more than 1";
                    }
                    signup = signups.get(0);

                    signup.getRoles()
                            .stream()
                            .filter(r -> r.getRole().equals(commandPair.getRight())).findFirst()
                            .orElseThrow(ExtractCommandException::new);


            }
        } catch (ExtractCommandException e) {

        }
        return null;
    }
}
